package io.github.crewhub.dto.gathering.response;

import io.github.crewhub.enums.gathering.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 내 모임 응답 데이터
 */
@Schema(description = "내가 가입한 모임 응답")
@Builder
public record MyGatheringResponse(
        @Schema(
                description = "모임 ID",
                example = "1"
        )
        Integer gatheringId,

        @Schema(
                description = "모임 이름",
                example = "백엔드 스터디"
        )
        String gatheringName,

        @Schema(
                description = "카테고리 이름",
                example = "개발"
        )
        String categoryLabel,

        @Schema(
                description = "내 역할",
                example = "MANAGER"
        )
        MemberRole role,

        @Schema(
                description = "가입 시각",
                example = "2026-06-01T19:00:00"
        )
        LocalDateTime joinedAt
) {
}
