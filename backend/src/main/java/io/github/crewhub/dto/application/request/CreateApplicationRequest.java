package io.github.crewhub.dto.application.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * 지원서 생성 요청 데이터
 */
@Schema(description = "모임 지원 요청")
@Builder
public record CreateApplicationRequest(

        @Schema(
                description = "지원할 모임 ID",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull Integer gatheringId
) {
}
