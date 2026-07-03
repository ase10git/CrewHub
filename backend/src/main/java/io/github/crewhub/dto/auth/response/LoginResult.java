package io.github.crewhub.dto.auth.response;

import lombok.Builder;

/**
 * 로그인 후 Access Token과 Refresh Token을 Controller로 전달하는 DTO
 */
@Builder
public record LoginResult(
        LoginResponse loginResponse,
        String refreshToken
) {
}
