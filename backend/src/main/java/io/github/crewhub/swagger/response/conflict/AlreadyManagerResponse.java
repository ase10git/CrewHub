package io.github.crewhub.swagger.response.conflict;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 중복 관리자 임명 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "409",
        description = "이미 관리자입니다."
)
public @interface AlreadyManagerResponse {
}
