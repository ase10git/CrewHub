package io.github.crewhub.swagger.response.unauthorized;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * Swagger
 * 로그인 오류 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "403",
        description = "이메일 또는 비밀번호가 올바르지 않습니다.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        implementation = ErrorResponse.class
                ),
                examples = @ExampleObject(
                        value = """
                                {
                                "success": false,
                                "message": "이메일 또는 비밀번호가 올바르지 않습니다.",
                                "code": "INVALID_LOGIN"
                                }
                                """
                )
        )
)
public @interface InvalidLoginResponse {
}
