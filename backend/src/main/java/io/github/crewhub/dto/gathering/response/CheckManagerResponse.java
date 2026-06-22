package io.github.crewhub.dto.gathering.response;

import io.github.crewhub.enums.gathering.MemberRole;
import lombok.Builder;

/**
 * 관리자 확인 응답 데이터
 */
@Builder
public record CheckManagerResponse(
        Integer gatheringId,
        Integer userId,
        boolean manager,
        MemberRole role
) {
}
