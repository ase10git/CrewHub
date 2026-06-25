package io.github.crewhub.swagger.annotation.gathering;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 모임 탈퇴 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "모임 탈퇴",
        description = """
                모임을 탈퇴합니다.
                """
)
@ApiResponse(
        responseCode = "200",
        description = "승인 성공"
)
public @interface LeaveGatheringApi {
}

