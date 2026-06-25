package io.github.crewhub.dto.application.response;

import io.github.crewhub.enums.application.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 내 지원서 조회 응답 데이터
 */
@Builder
@Schema(description = "내 지원서 정보")
public record MyApplicationResponse(
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
                example = "2026-06-20T18:00:00"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "최근 수정 일시",
                example = "2026-06-21T12:00:00"
        )
        LocalDateTime updatedAt
) {
}
