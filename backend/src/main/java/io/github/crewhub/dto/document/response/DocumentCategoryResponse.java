package io.github.crewhub.dto.document.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 카테고리 정보 응답 데이터
 */
@Builder
@Schema(description = "문서 카테고리 정보 응답")
public record DocumentCategoryResponse(

        @Schema(
                description = "카테고리 ID",
                example = "1"
        )
        Integer categoryId,

        @Schema(
                description = "카테고리 식별 키",
                example = "BACKEND"
        )
        String key,

        @Schema(
                description = "카테고리 표시명",
                example = "백엔드"
        )
        String label
) {
}
