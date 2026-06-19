package io.github.crewhub.dto.auth.response;

import lombok.Builder;

/**
 * 로그인 결과 데이터
 */
@Builder
public record LoginResponse(
    Integer userId,
    String username,
    String accessToken
) {
}
