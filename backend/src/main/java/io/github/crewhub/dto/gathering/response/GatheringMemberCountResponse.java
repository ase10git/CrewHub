package io.github.crewhub.dto.gathering.response;

import lombok.Builder;

/**
 * 모임 회원 수 응답 데이터
 */
@Builder
public record GatheringMemberCountResponse(
        Integer gatheringId,
        Long memberCount
) {
}
