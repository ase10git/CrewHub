package io.github.crewhub.swagger.annotation.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 지원서 취소 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "가입 신청 취소",
        description = """
                대기(PENDING) 상태의 신청서를 취소합니다.
                """
)
@ApiResponse(
        responseCode = "200",
        description = "신청 취소 성공"
)
public @interface ApplicationCancelApi {
}
