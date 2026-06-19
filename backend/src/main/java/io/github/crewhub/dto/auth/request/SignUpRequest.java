package io.github.crewhub.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청 데이터
 */
@Getter
@NoArgsConstructor
public class SignUpRequest {
    @Email
    private String email;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
