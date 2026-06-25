package io.github.crewhub.dto.document.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 내 문서 수 응답 데이터
 */
@Schema(description = "내 문서 수 조회 응답")
@Builder
public record MyDocumentCountResponse(
        @Schema(
                description = "사용자 ID",
                example = "5"
        )
        Integer userId,

        @Schema(
                description = "작성한 문서 개수",
                example = "12"
        )
        Long documentCount
) {
}
