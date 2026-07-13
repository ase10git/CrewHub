package io.github.crewhub.dto.auth.response;

import io.github.crewhub.dto.token.CsrfTokenInfo;
import io.github.crewhub.dto.token.RefreshTokenInfo;
import lombok.Builder;

/**
 * 인증 후 Access Token, Refresh Token, Csrf Token을 Controller로 전달하는 내부 DTO
 */
@Builder
public record AuthResult(
        AuthResponse authResponse,
        RefreshTokenInfo refreshTokenInfo,
        CsrfTokenInfo csrfTokenInfo
) {
}
