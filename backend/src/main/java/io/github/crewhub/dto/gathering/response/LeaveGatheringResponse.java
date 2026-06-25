package io.github.crewhub.dto.gathering.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 모임 탈퇴 응답 데이터
 */
@Schema(description = "모임 탈퇴 응답")
@Builder
public record LeaveGatheringResponse(
        @Schema(
                description = "모임 ID",
                example = "1"
        )
        Integer gatheringId,

        @Schema(
                description = "탈퇴한 회원 ID",
                example = "10"
        )
        Integer userId,

        @Schema(
                description = "결과 메시지",
                example = "모임에서 탈퇴했습니다."
        )
        String message,

        @Schema(
                description = "탈퇴 시각",
                example = "2026-06-25T14:35:00"
        )
        LocalDateTime leftAt
) {
}
