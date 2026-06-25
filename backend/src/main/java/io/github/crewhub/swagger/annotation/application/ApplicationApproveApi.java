package io.github.crewhub.swagger.annotation.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Swagger
 * 지원서 승인 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "가입 신청 승인",
        description = """
                모임 관리자가 가입 신청을 승인합니다.
                                
                승인 시 모임 멤버가 생성됩니다.
                """
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "승인 성공"
        ),
        @ApiResponse(
                responseCode = "403",
                description = "모임 관리자만 처리할 수 있습니다."
        ),
        @ApiResponse(
                responseCode = "404",
                description = "신청서를 찾을 수 없습니다."
        ),
        @ApiResponse(
                responseCode = "409",
                description = """
                이미 처리된 신청서입니다.
                또는 이미 가입된 사용자입니다.
                """
        )
})
public @interface ApplicationApproveApi {
}
