package io.github.crewhub.dto.gathering.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * 모임 생성 요청 데이터
 */
@Schema(description = "모임 생성 요청")
@Builder
public record CreateGatheringRequest(
        @Schema(
                description = "모임 이름",
                example = "백엔드 스터디"
        )
        @NotBlank @Size(min = 3, max = 100)
        String gatheringName,

        @Schema(
                description = "카테고리 ID",
                example = "1"
        )
        @NotNull
        Integer categoryId,

        @Schema(
                description = "모임 소개",
                example = "Spring Boot와 JPA를 함께 공부하는 스터디입니다."
        )
        @NotBlank @Size(min = 5, max = 500)
        String description
) {
}
