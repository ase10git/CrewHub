package io.github.crewhub.dto.gathering.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * 모임 수정 요청 데이터
 */
@Builder
public record UpdateGatheringRequest(
        @NotBlank
        @Size(min = 3, max = 100)
        String gatheringName,

        @NotBlank
        @Size(min = 5, max = 500)
        String description
) {
}