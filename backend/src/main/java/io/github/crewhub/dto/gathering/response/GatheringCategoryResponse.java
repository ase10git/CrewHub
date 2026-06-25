package io.github.crewhub.dto.gathering.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 모임 카테고리 정보 응답 데이터
 */
@Builder
@Schema(description = "모임 카테고리 정보 응답")
public record GatheringCategoryResponse(

        @Schema(
                description = "카테고리 ID",
                example = "1"
        )
        Integer categoryId,

        @Schema(
                description = "카테고리 식별 키",
                example = "study"
        )
        String key,

        @Schema(
                description = "카테고리 표시명",
                example = "스터디"
        )
        String label
) {
}
