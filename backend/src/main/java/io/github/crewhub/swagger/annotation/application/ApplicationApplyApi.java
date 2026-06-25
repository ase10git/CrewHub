package io.github.crewhub.swagger.annotation.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Swagger
 * 지원서 작성 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "모임 가입 신청",
        description = """
                사용자가 특정 모임에 가입 신청합니다.
                                
                이미 신청했거나,
                이미 모임 멤버인 경우 신청할 수 없습니다.
                """
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "가입 신청 성공"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "사용자 또는 모임을 찾을 수 없음"
        ),
        @ApiResponse(
                responseCode = "409",
                description = """
                이미 신청한 모임입니다.
                또는 이미 가입된 모임입니다.
                """
        )
})
public @interface ApplicationApplyApi {
}
