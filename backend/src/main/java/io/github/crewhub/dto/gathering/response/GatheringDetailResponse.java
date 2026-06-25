package io.github.crewhub.dto.gathering.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 모임 상세 정보 응답 데이터
 */
@Schema(description = "모임 상세 정보 응답")
@Builder
public record GatheringDetailResponse(
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
                description = "모임 설명",
                example = "Spring Boot와 JPA를 함께 공부하는 스터디입니다."
        )
        String description,

        @Schema(
                description = "카테고리 이름",
                example = "개발"
        )
        String categoryLabel,

        @Schema(
                description = "관리자 ID",
                example = "3"
        )
        Integer managerId,

        @Schema(
                description = "관리자 닉네임",
                example = "crewhub_manager"
        )
        String managerName
) {
}
