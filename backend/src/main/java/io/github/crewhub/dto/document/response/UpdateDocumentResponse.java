package io.github.crewhub.dto.document.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 문서 수정 응답 데이터
 */
@Builder
public record UpdateDocumentResponse(
        Integer documentId,
        String title,
        String content,
        List<String> categories,
        LocalDateTime updatedAt
) {
}
