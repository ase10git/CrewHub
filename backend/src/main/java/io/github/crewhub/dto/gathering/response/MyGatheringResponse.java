package io.github.crewhub.dto.gathering.response;

import io.github.crewhub.enums.gathering.MemberRole;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 내 모임 응답 데이터
 */
@Builder
public record MyGatheringResponse(
        Integer gatheringId,
        String gatheringName,
        String categoryLabel,
        MemberRole role,
        LocalDateTime joinedAt
) {
}
