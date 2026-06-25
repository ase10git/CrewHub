package io.github.crewhub.swagger.annotation.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "회원가입 성공"
        ),
        @ApiResponse(
                responseCode = "409",
                description = "중복 이메일 또는 닉네임"
        )
})
public @interface AuthRegisterApi {
}
