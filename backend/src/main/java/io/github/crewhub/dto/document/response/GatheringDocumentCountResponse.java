package io.github.crewhub.dto.document.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 모임 문서 수 조회 응답 데이터
 */
@Schema(description = "모임 문서 수 조회 응답")
@Builder
public record GatheringDocumentCountResponse(
        @Schema(
                description = "모임 ID",
                example = "10"
        )
        Integer gatheringId,

        @Schema(
                description = "모임 내 전체 문서 개수",
                example = "25"
        )
        Long documentCount
) {
}
