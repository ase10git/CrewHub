package io.github.crewhub.dto.auth.request;

import io.github.crewhub.config.auth.AuthValidationRegex;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * 회원가입 요청 데이터
 */
@Builder
@Schema(description = "회원가입 정보")
public record SignUpRequest (
        @Schema(
                description = "이메일",
                example = "test@test.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Email @NotBlank
        @Size(max = 100)
        String email,

        @Schema(
                description = "사용자 닉네임",
                example = "홍길동",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank @Size(min = 3, max = 20)
        @Pattern(
                regexp = AuthValidationRegex.USERNAME,
                message = "닉네임은 한글 또는 영어로 시작하고, 한글, 영어, 숫자, 밑줄(_)를 사용한 3 - 20자여야 합니다."
        )
        String username,

        @Schema(
                description = "비밀번호",
                example = "Password123!",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank @Size(min = 8, max = 100)
        @Pattern(
                regexp = AuthValidationRegex.PASSWORD,
                message = "비밀번호는 영문 대문자, 소문자, 숫자, 특수문자를 최소 1개씩 이상 포함한 8 - 100자여야 합니다."
        )
        String password
)
{}
