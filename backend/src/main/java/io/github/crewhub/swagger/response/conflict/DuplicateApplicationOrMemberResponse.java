package io.github.crewhub.swagger.response.conflict;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * Swagger
 * 중복 지원 혹은 이미 가입한 모임 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "409",
        description = "이미 지원했거나 가입한 모임입니다.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        implementation = ErrorResponse.class
                ),
                examples = {
                        @ExampleObject(
                                name = "DUPLICATE_APPLICATION",
                                summary = "중복 지원",
                                value = """
                                {
                                  "success": false,
                                  "message": "이미 지원한 모임입니다.",
                                  "code": "DUPLICATE_APPLICATION"
                                }
                                """
                        ),
                        @ExampleObject(
                                name = "DUPLICATE_GATHERING_MEMBER",
                                summary = "중복 가입",
                                value = """
                                {
                                  "success": false,
                                  "message": "이미 가입한 모임입니다.",
                                  "code": "DUPLICATE_GATHERING_MEMBER"
                                }
                                """
                        )
                }
        )
)
public @interface DuplicateApplicationOrMemberResponse {
}
