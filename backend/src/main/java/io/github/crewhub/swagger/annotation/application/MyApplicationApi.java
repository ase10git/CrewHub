package io.github.crewhub.swagger.annotation.application;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Swagger
 * 내 지원서 조회 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "내 신청 목록 조회",
        description = """
                현재 로그인한 사용자의
                모임 가입 신청 목록을 페이징 조회합니다.
                """
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "조회 성공"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "사용자를 찾을 수 없음"
        )
})
public @interface MyApplicationApi {
}
