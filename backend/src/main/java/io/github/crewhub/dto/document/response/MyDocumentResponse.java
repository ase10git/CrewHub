package io.github.crewhub.dto.document.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 내 문서 응답 데이터
 */
@Builder
public record MyDocumentResponse(
        Integer documentId,
        Integer gatheringId,
        String gatheringName,
        String title,
        Integer views,
        boolean isDeleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
