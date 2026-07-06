package io.github.crewhub.dto.token;

import lombok.Builder;

/**
 * 생성한 Csrf Token 정보 전달용 DTO
 */
@Builder
public record CsrfTokenInfo(
        String csrfToken,
        Long ttlSeconds
) {
}
