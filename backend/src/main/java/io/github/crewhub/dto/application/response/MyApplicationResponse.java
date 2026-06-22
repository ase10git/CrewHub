package io.github.crewhub.dto.application.response;

import io.github.crewhub.enums.application.ApplicationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 내 지원서 조회 응답 데이터
 */
@Builder
public record MyApplicationResponse(
        Integer applicationId,
        Integer gatheringId,
        String gatheringName,
        ApplicationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
