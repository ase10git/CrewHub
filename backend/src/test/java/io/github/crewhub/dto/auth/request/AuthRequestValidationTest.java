package io.github.crewhub.dto.auth.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class AuthRequestValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @AfterAll
    static void tearDown() {
        validator.close();
    }

    @Test
    void 회원가입_정상_입력은_검증을_통과한다() {
        SignUpRequest request = SignUpRequest.builder()
                .email("test@test.com")
                .username("홍길동_123")
                .password("Password123!")
                .build();

        Set<ConstraintViolation<SignUpRequest>> violations =
                validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void 회원가입_닉네임_최소_길이_경계값은_통과한다() {
        SignUpRequest request = SignUpRequest.builder()
                .email("test@test.com")
                .username("홍길")
                .password("Password123!")
                .build();

        Set<ConstraintViolation<SignUpRequest>> violations =
                validator.validate(request);

        assertThat(violations)
                .extracting(ConstraintViolation::getPropertyPath)
                .doesNotContain("username");
    }

    @Test
    void 회원가입_닉네임에_허용되지_않는_문자가_포함되면_실패한다() {
        SignUpRequest request = SignUpRequest.builder()
                .email("test@test.com")
                .username("홍길동!")
                .password("Password123!")
                .build();

        Set<ConstraintViolation<SignUpRequest>> violations =
                validator.validate(request);

        assertThat(violations)
                .extracting(ConstraintViolation::getPropertyPath)
                .contains("username");
    }

    @Test
    void 로그인_이메일이_100자를_초과하면_실패한다() {
        String email = "a".repeat(92) + "@test.com";
        LoginRequest request = LoginRequest.builder()
                .email(email)
                .password("Password123!")
                .build();

        Set<ConstraintViolation<LoginRequest>> violations =
                validator.validate(request);

        assertThat(violations)
                .extracting(ConstraintViolation::getPropertyPath)
                .contains("email");
    }
}
