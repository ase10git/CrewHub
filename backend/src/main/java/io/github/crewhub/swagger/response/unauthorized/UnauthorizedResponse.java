package io.github.crewhub.swagger.response.unauthorized;

import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
                description = "인증 실패",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                                implementation = ErrorResponse.class
                        ),
                        examples = @ExampleObject(
                                value = """
                        {
                          "success": false,
                          "message": "유효하지 않은 토큰입니다.",
                          "code": "INVALID_TOKEN"
                        }
                        """
                        )
                )
        ),
        @ApiResponse(
                responseCode = "403",
                description = "접근 권한이 없습니다.",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(
                                implementation = ErrorResponse.class
                        ),
                        examples = @ExampleObject(
                                value = """
                        {
                          "success": false,
                          "message": "접근 권한이 없습니다.",
                          "code": "ACCESS_DENIED"
                        }
                        """
                        )
                )
        )
})
@SecurityRequirement(name = "Bearer Authentication")
public @interface UnauthorizedResponse {
}
