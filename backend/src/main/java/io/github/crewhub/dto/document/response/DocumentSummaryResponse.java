package io.github.crewhub.dto.document.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 문서 목록 정보 응답 데이터
 */
@Schema(description = "문서 목록 응답")
@Builder
public record DocumentSummaryResponse(
        @Schema(
                description = "문서 ID",
                example = "100"
        )
        Integer documentId,

        @Schema(
                description = "문서 제목",
                example = "Spring Security 인증 구조"
        )
        String title,

        @Schema(
                description = "작성자 ID",
                example = "5"
        )
        Integer writerId,

        @Schema(
                description = "작성자 이름",
                example = "김개발"
        )
        String writerName,

        @Schema(
                description = "조회수",
                example = "230"
        )
        Integer views,

        @Schema(
                description = "작성 시간",
                example = "2026-06-25T10:30:00"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "수정 시간",
                example = "2026-06-25T12:00:00"
        )
        LocalDateTime updatedAt
) {
}
