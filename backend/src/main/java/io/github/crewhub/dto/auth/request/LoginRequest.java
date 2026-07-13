package io.github.crewhub.dto.auth.request;

import io.github.crewhub.config.auth.AuthValidationRegex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
        @Email @NotBlank
        @Size(max = 100)
        @Pattern(
                regexp = AuthValidationRegex.ENGLISH_EMAIL,
                message = "올바른 이메일 형식이 아닙니다."
        )
        String email,

        @Schema(
                description = "비밀번호",
                example = "Password123!",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank @Size(min = 8, max = 100)
        @Pattern(
                regexp = AuthValidationRegex.PASSWORD,
                message = "비밀번호는 영문 대문자, 소문자, 숫자, 특수문자(! @ # $ % ^ & *)를 최소 1개씩 이상 포함한 8 - 100자여야 합니다."
        )
        String password
) {
}
