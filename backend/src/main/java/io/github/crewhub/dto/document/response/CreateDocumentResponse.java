package io.github.crewhub.dto.document.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 문서 작성 응답 데이터
 */
@Builder
public record CreateDocumentResponse(
        Integer documentId,
        Integer gatheringId,
        String title,
        List<String> categories,
        LocalDateTime createdAt
) {
}
