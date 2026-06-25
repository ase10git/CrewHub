package io.github.crewhub.swagger.response.badrequest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.*;

/**
 * Swagger
 * 지원서 처리 오류 문서
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ApiResponse(
        responseCode = "400",
        description = "이미 처리된 지원서입니다."
)
public @interface ApplicationAlreadyProcessedResponse {
}
