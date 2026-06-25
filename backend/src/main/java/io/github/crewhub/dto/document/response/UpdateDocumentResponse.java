package io.github.crewhub.dto.document.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 문서 수정 응답 데이터
 */
@Schema(description = "문서 수정 응답")
@Builder
public record UpdateDocumentResponse(
        @Schema(
                description = "수정된 문서 ID",
                example = "100"
        )
        Integer documentId,

        @Schema(
                description = "수정된 문서 제목",
                example = "Spring Security 인증 흐름 정리"
        )
        String title,

        @Schema(
                description = "수정된 문서 내용",
                example = "Spring Security는 인증과 인가 기능을 제공합니다."
        )
        String content,

        @Schema(
                description = "수정된 카테고리 목록",
                example = "[\"SPRING\", \"SECURITY\"]"
        )
        List<String> categories,

        @Schema(
                description = "수정 완료 시간",
                example = "2026-06-25T15:30:00"
        )
        LocalDateTime updatedAt
) {
}
