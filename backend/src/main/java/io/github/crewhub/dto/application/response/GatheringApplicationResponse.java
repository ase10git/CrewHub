package io.github.crewhub.dto.application.response;

import io.github.crewhub.enums.application.ApplicationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 모임 지원서 조회 응답 데이터
 */
@Builder
public record GatheringApplicationResponse(
        Integer applicationId,
        Integer userId,
        String username,
        ApplicationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
