package io.github.crewhub.controller.auth;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.auth.request.LoginRequest;
import io.github.crewhub.dto.auth.request.SignUpRequest;
import io.github.crewhub.dto.auth.response.AuthResponse;
import io.github.crewhub.dto.auth.response.AuthResult;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.security.cookie.CookieProvider;
import io.github.crewhub.security.jwt.JwtProvider;
import io.github.crewhub.service.auth.AuthService;
import io.github.crewhub.service.auth.TokenService;
import io.github.crewhub.swagger.annotation.auth.AuthLoginApi;
import io.github.crewhub.swagger.annotation.auth.AuthRefreshApi;
import io.github.crewhub.swagger.annotation.auth.AuthRegisterApi;
import io.github.crewhub.swagger.response.conflict.DuplicateUserResponse;
import io.github.crewhub.swagger.response.notfound.UserNotFoundResponse;
import io.github.crewhub.swagger.response.unauthorized.InvalidLoginResponse;
import io.github.crewhub.swagger.response.unauthorized.InvalidTokenResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 인가 컨트롤러
 */
@Tag(
        name = "Auth",
        description = "회원가입 및 로그인 API"
)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenService tokenService;

    private final CookieProvider cookieProvider;
    private final JwtProvider jwtProvider;

    @AuthLoginApi
    @InvalidLoginResponse
    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
            ) {
        User user = authService.login(loginRequest);
        return authenticate(user, response);
    }

    @AuthRegisterApi
    @DuplicateUserResponse
    @PostMapping("/signup")
    public ApiResponse<AuthResponse> signup(
            @Valid @RequestBody SignUpRequest signUpRequest,
            HttpServletResponse response
    ) {
        User user = authService.signup(signUpRequest);
        return authenticate(user, response);
    }

    @AuthRefreshApi
    @InvalidTokenResponse
    @UserNotFoundResponse
    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        AuthResult authResult = tokenService.refresh(request);
        return responseWithToken(authResult, response);
    }

    private ApiResponse<AuthResponse> authenticate(
            User user,
            HttpServletResponse response
    ) {
        AuthResult result = tokenService.issueFirstTokens(user);

        return responseWithToken(result, response);
    }

    private ApiResponse<AuthResponse> responseWithToken(AuthResult result, HttpServletResponse response) {
        issueTokenCookie(result, response);

        return ApiResponse.success(result.authResponse());
    }

    private void issueTokenCookie(AuthResult result, HttpServletResponse response) {
        ResponseCookie refreshTokenCookie = cookieProvider.createRefreshTokenCookie(result.refreshTokenInfo());
        ResponseCookie csrfTokenCookie = cookieProvider.createCsrfTokenCookie(result.csrfTokenInfo());

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                refreshTokenCookie.toString()
        );
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                csrfTokenCookie.toString()
        );
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorization,
            HttpServletResponse response
    ) {
        String accessToken = jwtProvider.extractBearerToken(authorization);

        authService.logout(accessToken);

        ResponseCookie refreshTokenCookie = cookieProvider.deleteRefreshTokenCookie();
        ResponseCookie csrfTokenCookie = cookieProvider.deleteCsrfTokenCookie();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                refreshTokenCookie.toString()
        );
        response.addHeader(
                HttpHeaders.SET_COOKIE,
                csrfTokenCookie.toString()
        );

        return ApiResponse.success("성공적으로 로그아웃했습니다.");
    }
}
