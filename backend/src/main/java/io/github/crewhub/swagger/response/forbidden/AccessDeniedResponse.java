package io.github.crewhub.swagger.response.forbidden;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 권한 없음 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "403",
        description = "접근 권한이 없습니다."
)
public @interface AccessDeniedResponse {
}
