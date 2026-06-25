package io.github.crewhub.dto.gathering.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 모임 회원 수 응답 데이터
 */
@Schema(description = "모임 회원 수 응답")
@Builder
public record GatheringMemberCountResponse(
        @Schema(
                description = "모임 ID",
                example = "1"
        )
        Integer gatheringId,

        @Schema(
                description = "현재 가입 회원 수",
                example = "24"
        )
        Long memberCount
) {
}
