package io.github.crewhub.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * 회원가입 요청 데이터
 */
@Builder
public record SignUpRequest (
        @Email @NotBlank String email,

        @NotBlank @Size(min = 3, max = 100)
        String username,

        @NotBlank
        @Size(min = 8, max = 100)
        String password
)
{}
