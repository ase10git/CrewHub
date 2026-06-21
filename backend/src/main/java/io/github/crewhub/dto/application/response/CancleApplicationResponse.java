package io.github.crewhub.dto.application.response;

import io.github.crewhub.enums.application.ApplicationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 지원 취소 응답 데이터
 */
@Builder
public record CancleApplicationResponse(
        Integer applicationId,
        Integer gatheringId,
        ApplicationStatus status,
        LocalDateTime updatedAt
) {
}
