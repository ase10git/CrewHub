package io.github.crewhub.swagger.annotation.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "로그인 성공"
        ),
        @ApiResponse(
                responseCode = "401",
                description = "이메일 또는 비밀번호 오류"
        )
})
public @interface AuthLoginApi {
}