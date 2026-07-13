package io.github.crewhub.dto.auth.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 인증 결과 데이터
 */
@Builder
@Schema(description = "인증(로그인/회원가입) 결과")
public record AuthResponse(
        @Schema(
                description = "회원 ID",
                example = "1"
        )
        Integer userId,
        @Schema(
                description = "닉네임",
                example = "홍길동"
        )
        String username,
        @Schema(
                description = "JWT Access Token"
        )
        String accessToken
) {
}
