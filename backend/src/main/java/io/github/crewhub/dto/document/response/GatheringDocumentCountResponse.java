package io.github.crewhub.dto.document.response;

import lombok.Builder;

/**
 * 모임 문서 수 조회 응답 데이터
 */
@Builder
public record GatheringDocumentCountResponse(
        Integer gatheringId,
        Long documentCount
) {
}
