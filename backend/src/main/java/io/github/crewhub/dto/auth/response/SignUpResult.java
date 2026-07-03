package io.github.crewhub.dto.auth.response;

import lombok.Builder;

/**
 * 회원가입 후 Access Token과 Refresh Token을 Controller로 전달하는 DTO
 */
@Builder
public record SignUpResult(
        SignUpResponse signUpResponse,
        String refreshToken
) {
}
