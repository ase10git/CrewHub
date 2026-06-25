package io.github.crewhub.swagger.annotation.gathering;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 모임 수정 모임
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "모임 수정",
        description = """
                모임 관리자만 모임를 수정할 수 있습니다.
                """
)
@ApiResponse(
        responseCode = "200",
        description = "승인 성공"
)
public @interface UpdateGatheringApi {
}

