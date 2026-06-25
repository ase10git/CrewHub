package io.github.crewhub.swagger.response.badrequest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 관리자 위임 오류 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "400",
        description = "자기 자신에게 관리자 권한을 위임할 수 없습니다."
)
public @interface CannotTransferToSelfResponse {
}
