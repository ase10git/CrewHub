package io.github.crewhub.swagger.response.unauthorized;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * Swagger
 * 유효하지 않은 Token 응답 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "403",
        description = "유효하지 않은 토큰입니다.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        implementation = ErrorResponse.class
                ),
                examples = {
                        @ExampleObject(
                                name = "REFRESH_TOKEN_NOT_FOUND",
                                summary = "Refresh Token이 없음",
                                value = """
                                {
                                  "success": false,
                                  "message": "Refresh 토큰을 찾을 수 없습니다.",
                                  "code": "REFRESH_TOKEN_NOT_FOUND"
                                }
                                """
                        ),
                        @ExampleObject(
                                name = "EXPIRED_REFRESH_TOKEN",
                                summary = "만료된 Refresh Token",
                                value = """
                                {
                                  "success": false,
                                  "message": "만료된 Refresh 토큰입니다.",
                                  "code": "EXPIRED_REFRESH_TOKEN"
                                }
                                """
                        ),
                        @ExampleObject(
                                name = "EXPIRED_ACCESS_TOKEN",
                                summary = "만료된 Refresh Token",
                                value = """
                                {
                                  "success": false,
                                  "message": "만료된 Access 토큰입니다.",
                                  "code": "EXPIRED_ACCESS_TOKEN"
                                }
                                """
                        ),
                        @ExampleObject(
                                name = "INVALID_REFRESH_TOKEN",
                                summary = "유효하지 않은 Refresh Token",
                                value = """
                                {
                                  "success": false,
                                  "message": "유효하지 않은 Refresh 토큰입니다.",
                                  "code": "INVALID_REFRESH_TOKEN"
                                }
                                """
                        )
                }
        )
)
public @interface InvalidTokenResponse {
}
