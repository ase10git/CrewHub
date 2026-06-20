package io.github.crewhub.dto.gathering.response;

import lombok.Builder;

/**
 * 모임 수정 응답 데이터 
 */
@Builder
public record UpdateGatheringResponse(
        Integer gatheringId,
        String gatheringName,
        String description
) {
}