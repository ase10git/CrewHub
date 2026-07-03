package io.github.crewhub.controller.auth;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.auth.request.LoginRequest;
import io.github.crewhub.dto.auth.request.SignUpRequest;
import io.github.crewhub.dto.auth.response.AuthResponse;
import io.github.crewhub.dto.auth.response.AuthResult;
import io.github.crewhub.dto.token.RefreshTokenInfo;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.security.cookie.CookieProvider;
import io.github.crewhub.service.auth.AuthService;
import io.github.crewhub.service.auth.TokenService;
import io.github.crewhub.swagger.annotation.auth.AuthLoginApi;
import io.github.crewhub.swagger.annotation.auth.AuthRegisterApi;
import io.github.crewhub.swagger.response.conflict.DuplicateUserResponse;
import io.github.crewhub.swagger.response.unauthorized.InvalidLoginResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    private ApiResponse<AuthResponse> authenticate(User user, HttpServletResponse response) {
        AuthResult result = tokenService.issueAccessToken(user);

        return responseWithToken(result, response);
    }

    private ApiResponse<AuthResponse> responseWithToken(AuthResult result, HttpServletResponse response) {
        issueTokenCookie(
                result.refreshTokenInfo(),
                response
        );

        return ApiResponse.success(result.authResponse());
    }

    private void issueTokenCookie(RefreshTokenInfo refreshTokenInfo, HttpServletResponse response) {
        ResponseCookie cookie = cookieProvider.createRefreshTokenCookie(refreshTokenInfo);

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );
    }

}
