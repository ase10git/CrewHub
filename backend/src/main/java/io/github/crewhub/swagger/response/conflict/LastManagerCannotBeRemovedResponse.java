package io.github.crewhub.swagger.response.conflict;

import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 관리자 탈퇴 충돌 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "409",
        description = "모임에 관리자가 최소 1명 이상 존재해야 합니다.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        implementation = ErrorResponse.class
                ),
                examples = @ExampleObject(
                        value = """
                        {
                          "success": false,
                          "message": "모임에 관리자가 최소 1명 이상 존재해야 합니다.",
                          "code": "LAST_MANAGER_CANNOT_BE_REMOVED"
                        }
                        """
                )
        )
)
public @interface LastManagerCannotBeRemovedResponse {
}
