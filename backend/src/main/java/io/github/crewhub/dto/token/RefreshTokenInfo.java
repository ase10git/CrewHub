package io.github.crewhub.dto.token;

import lombok.Builder;

import java.time.Instant;

/**
 * 생성한 Refresh Token 정보 전달용 DTO
 */
@Builder
public record RefreshTokenInfo(
        String refreshToken,
        String jti,
        Instant expiresAt,
        Instant issuedAt,
        Long ttlSeconds
) {
}
