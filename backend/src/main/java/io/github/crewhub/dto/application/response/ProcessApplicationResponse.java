package io.github.crewhub.dto.application.response;

import io.github.crewhub.enums.application.ApplicationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 지원서 처리 응답 데이터
 */
@Builder
public record ProcessApplicationResponse(
        Integer applicationId,
        Integer gatheringId,
        Integer userId,
        ApplicationStatus status,
        LocalDateTime updatedAt
) {
}
