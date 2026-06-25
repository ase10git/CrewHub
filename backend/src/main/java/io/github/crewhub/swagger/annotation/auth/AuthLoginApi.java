package io.github.crewhub.swagger.annotation.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 로그인 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "로그인",
        description = "이메일과 비밀번호를 이용해 로그인합니다."
)
@ApiResponse(
        responseCode = "200",
        description = "로그인 성공"
)
public @interface AuthLoginApi {
}