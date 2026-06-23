package io.github.crewhub.dto.document.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 문서 목록 정보 응답 데이터
 */
@Builder
public record DocumentSummaryResponse(
        Integer documentId,
        String title,
        Integer writerId,
        String writerName,
        Integer views,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
