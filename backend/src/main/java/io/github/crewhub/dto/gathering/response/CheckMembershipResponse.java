package io.github.crewhub.dto.gathering.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 모임 가입 여부 응답 데이터
 */
@Schema(description = "모임 가입 여부 응답")
@Builder
public record CheckMembershipResponse(
        @Schema(
                description = "모임 ID",
                example = "1"
        )
        Integer gatheringId,

        @Schema(
                description = "사용자 ID",
                example = "10"
        )
        Integer userId,

        @Schema(
                description = "모임 가입 여부",
                example = "true"
        )
        boolean joined
) {
}
