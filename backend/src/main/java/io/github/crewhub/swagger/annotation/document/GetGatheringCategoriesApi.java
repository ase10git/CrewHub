package io.github.crewhub.swagger.annotation.document;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 모임 카테고리 조회 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "모임 카테고리 조회",
        description = """
                모임 카테고리 목록을 조회합니다.
                """
)
@ApiResponse(
        responseCode = "200",
        description = "승인 성공"
)
public @interface GetGatheringCategoriesApi {
}

