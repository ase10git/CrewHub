package io.github.crewhub.swagger.annotation.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 회원가입 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "회원가입",
        description = "신규 사용자를 생성합니다."
)
@ApiResponse(
        responseCode = "200",
        description = "회원가입 성공"
)
public @interface AuthRegisterApi {
}
