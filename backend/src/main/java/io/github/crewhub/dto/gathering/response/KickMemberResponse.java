package io.github.crewhub.dto.gathering.response;

import lombok.Builder;

/**
 * 회원 추방 응답 데이터
 */
@Builder
public record KickMemberResponse(
        Integer gatheringId,
        Integer userId,
        String username
) {
}
