package io.github.crewhub.swagger.response.notfound;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 카테고리 404 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "404",
        description = "카테고리를 찾을 수 없습니다."
)
public @interface CategoryNotFoundResponse {
}
