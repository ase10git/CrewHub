package io.github.crewhub.swagger.response.forbidden;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 작성자 권한 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "403",
        description = "작성자만 이용할 수 있습니다."
)
public @interface WriterOnlyResponse {
}
