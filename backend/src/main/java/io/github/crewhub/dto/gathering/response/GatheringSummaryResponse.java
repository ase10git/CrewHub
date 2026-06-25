package io.github.crewhub.dto.gathering.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 모임 정보 응답 데이터
 */
@Schema(description = "모임 요약 정보 응답")
@Builder
public record GatheringSummaryResponse(
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
                description = "관리자 닉네임",
                example = "crewhub_manager"
        )
        String managerName
) {
}
