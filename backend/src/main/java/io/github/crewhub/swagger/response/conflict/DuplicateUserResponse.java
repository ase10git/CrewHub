package io.github.crewhub.swagger.response.conflict;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * Swagger
 * 중복 유저 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "409",
        description = "중복 이메일 또는 사용자명입니다.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        implementation = ErrorResponse.class
                ),
                examples = {
                        @ExampleObject(
                                name = "DUPLICATE_EMAIL",
                                summary = "이메일 중복",
                                value = """
                                {
                                  "success": false,
                                  "message": "이미 사용중인 이메일입니다.",
                                  "code": "DUPLICATE_EMAIL"
                                }
                                """
                        ),
                        @ExampleObject(
                                name = "DUPLICATE_USERNAME",
                                summary = "닉네임 중복",
                                value = """
                                {
                                  "success": false,
                                  "message": "이미 사용중인 사용자명입니다.",
                                  "code": "DUPLICATE_USERNAME"
                                }
                                """
                        )
                }
        )
)
public @interface DuplicateUserResponse {
}
