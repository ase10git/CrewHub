package io.github.crewhub.swagger.response.notfound;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * Swagger
 * 모임, 문서, 카테고리 404 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "404",
        description = "모임 또는 문서 또는 카테고리를 찾을 수 없습니다.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        implementation = ErrorResponse.class
                ),
                examples = {
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
                        ),
                        @ExampleObject(
                                name = "DOCUMENT_NOT_FOUND",
                                summary = "문서 없음",
                                value = """
                                {
                                "success": false,
                                "message": "문서를 찾을 수 없습니다.",
                                "code": "DOCUMENT_NOT_FOUND"
                                }
                                """
                        ),
                        @ExampleObject(
                                name = "CATEGORY_NOT_FOUND",
                                summary = "카테고리 없음",
                                value = """
                                {
                                "success": false,
                                "message": "카테고리를 찾을 수 없습니다.",
                                "code": "CATEGORY_NOT_FOUND"
                                }
                                """
                        )
                }
        )
)
public @interface DocumentPropertiesNotFoundResponse {
}
