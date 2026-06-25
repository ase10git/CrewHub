package io.github.crewhub.swagger.response.notfound;

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
        description = "채팅방을 찾을 수 없습니다."
)
public @interface ChatroomNotFoundResponse {
}
