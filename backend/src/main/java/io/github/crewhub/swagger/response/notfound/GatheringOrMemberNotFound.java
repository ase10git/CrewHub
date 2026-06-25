package io.github.crewhub.swagger.response.notfound;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * Swagger
 * 모임 또는 회원 404 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "404",
        description = "모임 또는 회원을 찾을 수 없습니다.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        implementation = ErrorResponse.class
                ),
                examples = {
                        @ExampleObject(
                                name = "USER_NOT_FOUND",
                                summary = "사용자 없음",
                                value = """
                                {
                                  "success": false,
                                  "message": "사용자를 찾을 수 없습니다.",
                                  "code": "USER_NOT_FOUND"
                                }
                                """
                        ),
                        @ExampleObject(
                                name = "GATHERING_NOT_FOUND",
                                summary = "모임 없음",
                                value = """
                                {
                                  "success": false,
                                  "message": "모임을 찾을 수 없습니다.",
                                  "code": "GATHERING_NOT_FOUND"
                                }
                                """
                        )
                }
        )
)
public @interface GatheringOrMemberNotFound {
}
