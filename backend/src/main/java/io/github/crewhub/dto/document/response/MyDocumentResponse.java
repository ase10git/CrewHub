package io.github.crewhub.dto.document.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 내 문서 응답 데이터
 */
@Schema(description = "내 문서 목록 응답")
@Builder
public record MyDocumentResponse(
        @Schema(
                description = "문서 ID",
                example = "100"
        )
        Integer documentId,

        @Schema(
                description = "모임 ID",
                example = "10"
        )
        Integer gatheringId,

        @Schema(
                description = "모임 이름",
                example = "Spring Boot 스터디"
        )
        String gatheringName,

        @Schema(
                description = "문서 제목",
                example = "JPA 영속성 컨텍스트 정리"
        )
        String title,

        @Schema(
                description = "조회수",
                example = "150"
        )
        Integer views,

        @Schema(
                description = "삭제 여부",
                example = "false"
        )
        boolean isDeleted,

        @Schema(
                description = "문서 생성 시간",
                example = "2026-06-25T10:30:00"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "문서 수정 시간",
                example = "2026-06-25T12:00:00"
        )
        LocalDateTime updatedAt
) {
}
