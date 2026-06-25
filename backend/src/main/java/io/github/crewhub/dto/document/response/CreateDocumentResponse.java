package io.github.crewhub.dto.document.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 문서 작성 응답 데이터
 */
@Schema(description = "문서 작성 응답")
@Builder
public record CreateDocumentResponse(

        @Schema(
                description = "생성된 문서 ID",
                example = "100"
        )
        Integer documentId,

        @Schema(
                description = "소속 모임 ID",
                example = "10"
        )
        Integer gatheringId,

        @Schema(
                description = "문서 제목",
                example = "Spring Boot 스터디 정리"
        )
        String title,

        @Schema(
                description = "문서에 적용된 카테고리 목록",
                example = "[\"BACKEND\", \"SPRING\"]"
        )
        List<String> categories,

        @Schema(
                description = "문서 생성 시간",
                example = "2026-06-25T10:30:00"
        )
        LocalDateTime createdAt
) {
}
