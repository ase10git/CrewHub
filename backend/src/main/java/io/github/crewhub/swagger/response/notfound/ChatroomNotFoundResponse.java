package io.github.crewhub.swagger.response.notfound;

import io.github.crewhub.common.response.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 채팅방 404 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "404",
        description = "채팅방을 찾을 수 없습니다.",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(
                        implementation = ErrorResponse.class
                ),
                examples = @ExampleObject(
                        value = """
                        {
                          "success": false,
                          "message": "채팅방을 찾을 수 없습니다.",
                          "code": "CHATROOM_NOT_FOUND"
                        }
                        """
                )
        )
)
public @interface ChatroomNotFoundResponse {
}
