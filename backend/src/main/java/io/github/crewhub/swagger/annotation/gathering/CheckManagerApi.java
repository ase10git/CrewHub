package io.github.crewhub.swagger.annotation.gathering;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.*;

/**
 * Swagger
 * 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "모임 관리자 확인",
        description = """
                관리자 여부를 확인합니다
                """
)
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "승인 성공"
        )
})
public @interface CheckManagerApi {
}

