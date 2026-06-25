package io.github.crewhub.swagger.annotation.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
@ApiResponse(
        responseCode = "200",
        description = "승인 성공"
)
public @interface ApplicationApproveApi {
}
