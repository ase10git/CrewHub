package io.github.crewhub.dto.document.response;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 문서 상세 정보 응답 데이터
 */
@Builder
public record DocumentDetailResponse(
        Integer documentId,
        List<CategoryResponse> categoryList,
        Integer writerId,
        String writerName,
        Integer gatheringId,
        String gatheringName,
        String title,
        String content,
        Integer views,
        boolean isDeleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
