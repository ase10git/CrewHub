package io.github.crewhub.swagger.annotation.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * Token 재발급 요청 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "Token 재발급",
        description = """
                Refresh Token과 Access Token 재발급
                """
)
@ApiResponse(
        responseCode = "200",
        description = "승인 성공"
)
public @interface AuthRefreshApi {
}

