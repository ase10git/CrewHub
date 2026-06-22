package io.github.crewhub.dto.gathering.response;

import lombok.Builder;

/**
 * 모임 가입 여부 응답 데이터
 */
@Builder
public record CheckMembershipResponse(
        Integer gatheringId,
        Integer userId,
        boolean joined
) {
}
