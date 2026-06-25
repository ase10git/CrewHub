package io.github.crewhub.swagger.response.badrequest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 탈퇴 대상 오류 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "400",
        description = "자신은 강제 탈퇴시킬 수 없습니다."
)
public @interface CannotKickSelfResponse {
}
