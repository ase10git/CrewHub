package io.github.crewhub.swagger.response.forbidden;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * Swagger
 * 회원 또는 작성자 권한 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "403",
        description = "회원 또는 작성자만 이용할 수 있습니다.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        implementation = ErrorResponse.class
                ),
                examples = {
                        @ExampleObject(
                                name = "GATHERING_MEMBER_ONLY",
                                summary = "모임 회원 권한",
                                value = """
                                {
                                "success": false,
                                "message": "모임 회원만 이용할 수 있습니다.",
                                "code": "GATHERING_MEMBER_ONLY"
                                }
                                """
                        ),
                        @ExampleObject(
                                name = "WRITER_ONLY",
                                summary = "작성자 권한",
                                value = """
                                {
                                "success": false,
                                "message": "작성자만 이용할 수 있습니다.",
                                "code": "WRITER_ONLY"
                                }
                                """
                        )
                }
        )
)
public @interface MemberOrWriterOnlyResponse {
}
