package io.github.crewhub.dto.application.response;

import io.github.crewhub.enums.application.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 지원서 처리 응답 데이터
 */
@Builder
@Schema(description = "지원 승인/거절 결과")
public record ProcessApplicationResponse(
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
                description = "지원자 ID",
                example = "8"
        )
        Integer userId,

        @Schema(
                description = "처리 후 상태",
                example = "APPROVED"
        )
        ApplicationStatus status,

        @Schema(
                description = "처리 일시",
                example = "2026-06-25T15:20:00"
        )
        LocalDateTime updatedAt
) {
}
