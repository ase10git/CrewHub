package io.github.crewhub.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * 로그인 요청 데이터
 */
@Builder
@Schema(description = "로그인 요청")
public record LoginRequest (
        @Schema(
                description = "로그인 이메일",
                example = "test@test.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank @Email String email,
        @Schema(
                description = "비밀번호",
                example = "password123!",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank String password
) {
}
