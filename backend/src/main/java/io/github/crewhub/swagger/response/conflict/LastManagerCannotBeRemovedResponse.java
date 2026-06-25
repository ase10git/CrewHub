package io.github.crewhub.swagger.response.conflict;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 관리자 탈퇴 충돌 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "409",
        description = "모임에 관리자가 최소 1명 이상 존재해야 합니다."
)
public @interface LastManagerCannotBeRemovedResponse {
}
