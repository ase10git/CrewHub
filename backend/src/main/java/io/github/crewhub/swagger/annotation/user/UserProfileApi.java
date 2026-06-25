package io.github.crewhub.swagger.annotation.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
@ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "프로필 조회 성공"
        ),
        @ApiResponse(
                responseCode = "404",
                description = "사용자를 찾을 수 없음"
        )
})
public @interface UserProfileApi {
}
