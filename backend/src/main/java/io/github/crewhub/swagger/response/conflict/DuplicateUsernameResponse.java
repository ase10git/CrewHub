package io.github.crewhub.swagger.response.conflict;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 중복 사용자명 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "409",
        description = "이미 사용중인 사용자명입니다."
)
public @interface DuplicateUsernameResponse {
}
