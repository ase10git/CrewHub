package io.github.crewhub.dto.gathering.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 관리자 변경 응답 데이터
 */
@Schema(description = "모임 관리자 변경 응답")
@Builder
public record TransferManagerResponse(
        @Schema(
                description = "모임 ID",
                example = "1"
        )
        Integer gatheringId,

        @Schema(
                description = "기존 관리자 ID",
                example = "10"
        )
        Integer previousManagerId,

        @Schema(
                description = "새 관리자 ID",
                example = "15"
        )
        Integer newManagerId,

        @Schema(
                description = "새 관리자 닉네임",
                example = "crew_master"
        )
        String newManagerName,

        @Schema(
                description = "관리자 권한 이양 시각",
                example = "2026-06-25T14:30:00"
        )
        LocalDateTime transferredAt
) {
}
