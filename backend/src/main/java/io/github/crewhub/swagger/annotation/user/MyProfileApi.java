package io.github.crewhub.swagger.annotation.user;

import io.github.crewhub.swagger.response.UnauthorizedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 내 프로필 조회 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "내 프로필 조회",
        description = "현재 로그인한 사용자의 프로필 정보를 조회합니다."
)
@ApiResponse(
        responseCode = "200",
        description = "프로필 조회 성공"
)
@UnauthorizedResponse
public @interface MyProfileApi {
}
