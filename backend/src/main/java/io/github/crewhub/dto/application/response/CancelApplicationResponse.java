package io.github.crewhub.dto.application.response;

import io.github.crewhub.enums.application.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 지원 취소 응답 데이터
 */
@Builder
@Schema(description = "지원 취소 결과")
public record CancelApplicationResponse(
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
                description = "변경된 지원 상태",
                example = "CANCELLED"
        )
        ApplicationStatus status,

        @Schema(
                description = "상태 변경 시각",
                example = "2026-06-25T15:10:00"
        )
        LocalDateTime updatedAt
) {
}
