package io.github.crewhub.swagger.response.forbidden;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 모임 관리자 탈퇴 차단 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "403",
        description = "모임 관리자는 탈퇴할 수 없습니다. 먼저 관리자 권한을 위임하세요."
)
public @interface ManagerCannotLeaveResponse {
}
