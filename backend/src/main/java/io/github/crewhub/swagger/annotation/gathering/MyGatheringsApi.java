package io.github.crewhub.swagger.annotation.gathering;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 내 모임 조회 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "내 모임 조회",
        description = """
                현재 가입한 모임 목록을 조회합니다.
                """
)
@ApiResponse(
        responseCode = "200",
        description = "승인 성공"
)
public @interface MyGatheringsApi {
}

