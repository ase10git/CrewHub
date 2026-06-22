package io.github.crewhub.dto.gathering.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 모임 탈퇴 응답 데이터
 */
@Builder
public record LeaveGatheringResponse(
        Integer gatheringId,
        Integer userId,
        String message,
        LocalDateTime leftAt
) {
}
