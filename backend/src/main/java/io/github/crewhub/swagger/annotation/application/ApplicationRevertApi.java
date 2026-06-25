package io.github.crewhub.swagger.annotation.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 지원서 취소 복구 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "가입 신청 복구",
        description = """
                취소(CANCELLED) 상태의 신청서를
                다시 대기(PENDING) 상태로 변경합니다.
                """
)
@ApiResponse(
        responseCode = "200",
        description = "신청 복구 성공"
)
public @interface ApplicationRevertApi {
}
