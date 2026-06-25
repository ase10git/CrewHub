package io.github.crewhub.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * API 공통 응답용 클래스
 * @param <T>
 */
@Schema(description = "API 공통 응답 데이터")
@Getter
@Builder
public class ApiResponse<T> {

    @Schema(
            description = "요청 처리 성공 여부",
            example = "true"
    )
    private final boolean success;

    @Schema(
            description = "응답 메시지",
            example = "요청이 성공적으로 처리되었습니다."
    )
    private final String message;

    @Schema(
            description = "응답 데이터"
    )
    private final T data;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message("요청이 성공적으로 처리되었습니다.")
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }
}