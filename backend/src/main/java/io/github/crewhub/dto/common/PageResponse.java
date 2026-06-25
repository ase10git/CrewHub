package io.github.crewhub.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.List;

/**
 * 페이지 응답 
 */
@Schema(description = "페이지 응답 데이터")
@Builder
public record PageResponse<T>(

        @Schema(
                description = "조회 결과 목록"
        )
        List<T> content,

        @Schema(
                description = "현재 페이지 번호 (0부터 시작)",
                example = "0"
        )
        int page,

        @Schema(
                description = "페이지 크기",
                example = "20"
        )
        int size,

        @Schema(
                description = "전체 데이터 개수",
                example = "120"
        )
        long totalElements,

        @Schema(
                description = "전체 페이지 수",
                example = "6"
        )
        int totalPages,

        @Schema(
                description = "다음 페이지 존재 여부",
                example = "true"
        )
        boolean hasNext
) {
}