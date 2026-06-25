package io.github.crewhub.swagger.annotation.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "신청 취소 성공"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "신청서를 찾을 수 없음"
        ),
        @ApiResponse(
                responseCode = "409",
                description = "이미 처리된 신청서입니다."
        )
})
public @interface ApplicationCancelApi {
}
