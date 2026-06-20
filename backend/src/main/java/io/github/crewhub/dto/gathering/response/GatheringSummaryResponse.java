package io.github.crewhub.dto.gathering.response;

import lombok.Builder;

/**
 * 모임 정보 응답 데이터
 */
@Builder
public record GatheringSummaryResponse(
        Integer gatheringId,
        String gatheringName,
        String categoryLabel,
        String managerName
) {
}
