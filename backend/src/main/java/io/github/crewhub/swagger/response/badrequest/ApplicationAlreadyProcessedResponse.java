package io.github.crewhub.swagger.response.badrequest;

import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 지원서 처리 오류 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "400",
        description = "이미 처리된 지원서입니다.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        implementation = ErrorResponse.class
                ),
                examples = @ExampleObject(
                        value = """
                        {
                          "success": false,
                          "message": "이미 처리된 지원서입니다.",
                          "code": "APPLICATION_ALREADY_PROCESSED"
                        }
                        """
                )
        )
)
public @interface ApplicationAlreadyProcessedResponse {
}
