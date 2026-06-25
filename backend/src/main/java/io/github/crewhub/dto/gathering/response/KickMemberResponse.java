package io.github.crewhub.dto.gathering.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 회원 추방 응답 데이터
 */
@Schema(description = "회원 추방 응답")
@Builder
public record KickMemberResponse(
        @Schema(
                description = "모임 ID",
                example = "1"
        )
        Integer gatheringId,

        @Schema(
                description = "추방된 회원 ID",
                example = "15"
        )
        Integer userId,

        @Schema(
                description = "추방된 회원 닉네임",
                example = "kicked_user"
        )
        String username
) {
}
