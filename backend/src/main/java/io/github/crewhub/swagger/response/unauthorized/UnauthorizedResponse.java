package io.github.crewhub.swagger.response.unauthorized;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.*;

/**
 * Swagger
 * 401 응답 문서
 */
@Target({
        ElementType.TYPE,
        ElementType.METHOD,
        ElementType.ANNOTATION_TYPE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponses({
        @ApiResponse(
                responseCode = "401",
                description = "인증 실패"
        ),
        @ApiResponse(
                responseCode = "403",
                description = "접근 권한이 없습니다."
        )
})
@SecurityRequirement(name = "Bearer Authentication")
public @interface UnauthorizedResponse {
}
