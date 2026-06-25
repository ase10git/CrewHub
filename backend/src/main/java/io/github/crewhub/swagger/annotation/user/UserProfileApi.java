package io.github.crewhub.swagger.annotation.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 사용자 프로필 조회 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "사용자 프로필 조회",
        description = "사용자 ID를 통해 프로필 정보를 조회합니다."
)
@ApiResponse(
        responseCode = "200",
        description = "프로필 조회 성공"
)
public @interface UserProfileApi {
}
