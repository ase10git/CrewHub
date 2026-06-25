package io.github.crewhub.swagger.annotation.gathering;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 회원 추방 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "회원 추방",
        description = """
                모임 관리자가 회원을 추방시킵니다.
                """
)
@ApiResponse(
        responseCode = "200",
        description = "승인 성공"
)
public @interface KickMemberApi {
}

