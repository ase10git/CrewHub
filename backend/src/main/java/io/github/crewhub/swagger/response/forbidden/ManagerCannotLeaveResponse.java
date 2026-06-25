package io.github.crewhub.swagger.response.forbidden;

import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 모임 관리자 탈퇴 차단 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "403",
        description = "모임 관리자는 탈퇴할 수 없습니다. 먼저 관리자 권한을 위임하세요.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        implementation = ErrorResponse.class
                ),
                examples = @ExampleObject(
                        value = """
                        {
                          "success": false,
                          "message": "모임 관리자는 탈퇴할 수 없습니다. 먼저 관리자 권한을 위임하세요.",
                          "code": "MANAGER_CANNOT_LEAVE"
                        }
                        """
                )
        )
)
public @interface ManagerCannotLeaveResponse {
}
