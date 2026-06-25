package io.github.crewhub.swagger.response.badrequest;

import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 관리자 위임 오류 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "400",
        description = "자기 자신에게 관리자 권한을 위임할 수 없습니다.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        implementation = ErrorResponse.class
                ),
                examples = @ExampleObject(
                        value = """
                        {
                          "success": false,
                          "message": "자기 자신에게 관리자 권한을 위임할 수 없습니다.",
                          "code": "CANNOT_TRANSFER_TO_SELF"
                        }
                        """
                )
        )
)
public @interface CannotTransferToSelfResponse {
}
