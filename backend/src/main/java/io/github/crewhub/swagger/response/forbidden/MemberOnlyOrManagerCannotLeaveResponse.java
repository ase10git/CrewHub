package io.github.crewhub.swagger.response.forbidden;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.lang.annotation.*;

/**
 * Swagger
 * 모임 회원 권한 혹은 관리자 탈퇴 차단 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "403",
        description = "모임 회원만 가능하거나 마지막 관리자는 탈퇴할 수 없습니다.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        implementation = ErrorResponse.class
                ),
                examples = {
                        @ExampleObject(
                                name = "GATHERING_MEMBER_ONLY",
                                summary = "모임 회원만 가능",
                                value = """
                                {
                                  "success": false,
                                  "message": "모임 회원만 이용할 수 있습니다.",
                                  "code": "GATHERING_MEMBER_ONLY"
                                }
                                """
                        ),
                        @ExampleObject(
                                name = "MANAGER_CANNOT_LEAVE",
                                summary = "관리자 탈퇴 불가",
                                value = """
                                {
                                  "success": false,
                                  "message": "모임 관리자는 탈퇴할 수 없습니다. 먼저 관리자 권한을 위임하세요.",
                                  "code": "MANAGER_CANNOT_LEAVE"
                                }
                                """
                        )
                }
        )
)
public @interface MemberOnlyOrManagerCannotLeaveResponse {
}
