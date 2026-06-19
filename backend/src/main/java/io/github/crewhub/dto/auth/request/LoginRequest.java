package io.github.crewhub.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * 로그인 요청 데이터
 */
@Builder
public record LoginRequest (
        @NotBlank @Email String email,
        @NotBlank String password
) {
}
