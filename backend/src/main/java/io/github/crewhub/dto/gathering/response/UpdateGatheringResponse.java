package io.github.crewhub.dto.gathering.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 모임 수정 응답 데이터 
 */
@Schema(description = "모임 수정 응답")
@Builder
public record UpdateGatheringResponse(
        @Schema(
                description = "모임 ID",
                example = "1"
        )
        Integer gatheringId,

        @Schema(
                description = "수정된 모임명",
                example = "백엔드 스터디"
        )
        String gatheringName,

        @Schema(
                description = "수정된 모임 설명",
                example = "Spring Boot와 JPA를 함께 공부하는 모임입니다."
        )
        String description
) {
}