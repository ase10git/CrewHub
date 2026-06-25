package io.github.crewhub.swagger.annotation.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 지원서 거절 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "가입 신청 거절",
        description = """
                모임 관리자가 가입 신청을 거절합니다.
                """
)
@ApiResponse(
        responseCode = "200",
        description = "거절 성공"
)
public @interface ApplicationRejectApi {
}
