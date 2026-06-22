package io.github.crewhub.dto.application.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * 지원서 생성 요청 데이터
 */
@Builder
public record CreateApplicationRequest(
        @NotNull Integer gatheringId
) {
}
