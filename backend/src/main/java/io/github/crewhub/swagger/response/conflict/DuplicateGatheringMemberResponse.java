package io.github.crewhub.swagger.response.conflict;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 중복 모임 회원 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "409",
        description = "이미 가입한 모임입니다."
)
public @interface DuplicateGatheringMemberResponse {
}
