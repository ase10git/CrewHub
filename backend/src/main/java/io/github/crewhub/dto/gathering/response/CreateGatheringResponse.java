package io.github.crewhub.dto.gathering.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 모임 생성 응답 데이터
 */
@Schema(description = "모임 생성 응답")
@Builder
public record CreateGatheringResponse(
        @Schema(
                description = "생성된 모임 ID",
                example = "1"
        )
        Integer gatheringId,

        @Schema(
                description = "모임 이름",
                example = "백엔드 스터디"
        )
        String gatheringName
) {
}
