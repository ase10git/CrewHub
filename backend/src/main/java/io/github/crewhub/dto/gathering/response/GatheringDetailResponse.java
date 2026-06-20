package io.github.crewhub.dto.gathering.response;

import lombok.Builder;

/**
 * 모임 상세 정보 응답 데이터
 */
@Builder
public record GatheringDetailResponse(
        Integer gatheringId,
        String gatheringName,
        String description,
        String categoryLabel,
        Integer managerId,
        String managerName
) {
}
