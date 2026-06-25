package io.github.crewhub.swagger.response.forbidden;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 모임 관리자 권한 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "403",
        description = "모임 관리자만 가능합니다."
)
public @interface GatheringManagerOnlyResponse {
}
