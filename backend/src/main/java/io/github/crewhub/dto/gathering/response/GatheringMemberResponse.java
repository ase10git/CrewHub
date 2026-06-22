package io.github.crewhub.dto.gathering.response;

import io.github.crewhub.enums.gathering.MemberRole;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 모임 회원 정보 응답 데이터
 */
@Builder
public record GatheringMemberResponse(
        Integer userId,
        String username,
        MemberRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
