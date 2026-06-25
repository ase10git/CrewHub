package io.github.crewhub.dto.application.response;

import io.github.crewhub.enums.application.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 지원서 생성 응답 데이터
 */
@Builder
@Schema(description = "지원서 생성 결과")
public record CreateApplicationResponse(
        @Schema(
                description = "지원서 ID",
                example = "15"
        )
        Integer applicationId,

        @Schema(
                description = "모임 ID",
                example = "3"
        )
        Integer gatheringId,

        @Schema(
                description = "모임명",
                example = "Spring Boot 스터디"
        )
        String gatheringName,

        @Schema(
                description = "지원 상태",
                example = "PENDING"
        )
        ApplicationStatus status,

        @Schema(
                description = "지원 일시",
                example = "2026-06-25T14:30:00"
        )
        LocalDateTime createdAt
) {
}
