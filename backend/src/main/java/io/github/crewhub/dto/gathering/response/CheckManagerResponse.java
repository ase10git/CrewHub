package io.github.crewhub.dto.gathering.response;

import io.github.crewhub.enums.gathering.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 관리자 확인 응답 데이터
 */
@Schema(description = "모임 관리자 여부 확인 응답")
@Builder
public record CheckManagerResponse(
        @Schema(
                description = "모임 ID",
                example = "1"
        )
        Integer gatheringId,

        @Schema(
                description = "사용자 ID",
                example = "3"
        )
        Integer userId,

        @Schema(
                description = "관리자 여부",
                example = "true"
        )
        boolean manager,

        @Schema(
                description = "모임 내 역할",
                example = "MANAGER"
        )
        MemberRole role
) {
}
