package io.github.crewhub.dto.gathering.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * 모임 수정 요청 데이터
 */
@Schema(description = "모임 수정 요청")
@Builder
public record UpdateGatheringRequest(
        @Schema(
                description = "수정할 모임 이름",
                example = "백엔드 심화 스터디"
        )
        @NotBlank @Size(min = 3, max = 100)
        String gatheringName,

        @Schema(
                description = "수정할 모임 소개",
                example = "Spring Security와 QueryDSL까지 함께 학습합니다."
        )
        @NotBlank @Size(min = 5, max = 500)
        String description
) {
}