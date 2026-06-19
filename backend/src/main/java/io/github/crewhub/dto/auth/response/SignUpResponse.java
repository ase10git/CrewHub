package io.github.crewhub.dto.auth.response;

import lombok.Builder;

/**
 * 회원가입 결과 데이터
 */
@Builder
public record SignUpResponse(
        Integer userId,
        String username,
        String accessToken
) {
}
