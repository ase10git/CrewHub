package io.github.crewhub.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * API 공통 에러 응답 클래스
 */
@Schema(description = "공통 오류 응답")
@Getter
@Builder
public class ErrorResponse {

    @Schema(
            description = "요청 성공 여부",
            example = "false"
    )
    private final boolean success;

    @Schema(
            description = "오류 코드"
    )
    private final String code;

    @Schema(
            description = "오류 메시지"
    )
    private final String message;
}