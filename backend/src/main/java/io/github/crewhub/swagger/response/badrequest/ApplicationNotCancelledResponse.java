package io.github.crewhub.swagger.response.badrequest;

import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 취소되지 않은 지원서 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "400",
        description = "취소된 지원서만 복구할 수 있습니다.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        implementation = ErrorResponse.class
                ),
                examples = @ExampleObject(
                        value = """
                        {
                          "success": false,
                          "message": "취소된 지원서만 복구할 수 있습니다.",
                          "code": "APPLICATION_NOT_CANCELLED"
                        }
                        """
                )
        )
)
public @interface ApplicationNotCancelledResponse {
}
