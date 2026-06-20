package io.github.crewhub.dto.gathering.response;

import lombok.Builder;

/**
 * 모임 생성 응답 데이터
 */
@Builder
public record CreateGatheringResponse(
        Integer gatheringId,
        String gatheringName
) {
}
