package io.github.crewhub.dto.application.response;

import io.github.crewhub.enums.application.ApplicationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 지원서 생성 응답 데이터
 */
@Builder
public record CreateApplicationResponse(
        Integer applicationId,
        Integer gatheringId,
        String gatheringName,
        ApplicationStatus status,
        LocalDateTime createdAt
) {
}
