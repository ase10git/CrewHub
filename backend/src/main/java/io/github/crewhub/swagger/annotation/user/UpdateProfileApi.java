package io.github.crewhub.swagger.annotation.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 사용자 프로필 수정 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Operation(
        summary = "프로필 수정",
        description = "사용자의 프로필 정보를 수정합니다."
)
@ApiResponse(
        responseCode = "200",
        description = "프로필 수정 성공"
)
public @interface UpdateProfileApi {
}
