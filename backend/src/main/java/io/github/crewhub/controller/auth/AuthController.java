package io.github.crewhub.controller.auth;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.auth.request.LoginRequest;
import io.github.crewhub.dto.auth.request.SignUpRequest;
import io.github.crewhub.dto.auth.response.LoginResponse;
import io.github.crewhub.dto.auth.response.SignUpResponse;
import io.github.crewhub.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 인가 컨트롤러
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest
            ) {
        return ApiResponse.success(authService.login(loginRequest));
    }

    @PostMapping("/signup")
    public ApiResponse<SignUpResponse> signup(
            @Valid @RequestBody SignUpRequest signUpRequest
    ) {
        return ApiResponse.success(authService.signup(signUpRequest));
    }

}
