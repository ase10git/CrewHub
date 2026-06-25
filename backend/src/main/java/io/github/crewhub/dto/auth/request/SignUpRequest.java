package io.github.crewhub.dto.auth.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * 회원가입 요청 데이터
 */
@Builder
public record SignUpRequest (
        @Schema(
                description = "이메일",
                example = "test@test.com"
        )
        @Email @NotBlank String email,

        @Schema(
                description = "사용자 닉네임",
                example = "홍길동"
        )
        @NotBlank @Size(min = 3, max = 100)
        String username,

        @Schema(
                description = "비밀번호",
                example = "password123!"
        )
        @NotBlank @Size(min = 8, max = 100)
        String password
)
{}
