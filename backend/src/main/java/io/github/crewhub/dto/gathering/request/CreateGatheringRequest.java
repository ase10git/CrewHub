package io.github.crewhub.dto.gathering.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * 모임 생성 요청 데이터
 */
@Builder
public record CreateGatheringRequest(
        @NotBlank @Size(min = 3, max = 100)
        String gatheringName,
        @NotNull
        Integer categoryId,
        @NotBlank @Size(min = 5, max = 500)
        String description
) {
}
