package io.github.crewhub.swagger.annotation.gathering;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 모임 조회 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "모임 목록 조회",
        description = "전체 활성 모임 목록을 페이징하여 조회합니다."
)
@ApiResponse(
        responseCode = "200",
        description = "모임 목록 조회 성공"
)
public @interface GetGatheringsApi {
}
