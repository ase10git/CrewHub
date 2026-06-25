package io.github.crewhub.dto.application.response;

import io.github.crewhub.enums.application.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 모임 지원서 조회 응답 데이터
 */
@Builder
@Schema(description = "모임 지원자 정보")
public record GatheringApplicationResponse(
        @Schema(
                description = "지원서 ID",
                example = "15"
        )
        Integer applicationId,

        @Schema(
                description = "지원자 ID",
                example = "8"
        )
        Integer userId,

        @Schema(
                description = "지원자 닉네임",
                example = "홍길동"
        )
        String username,

        @Schema(
                description = "지원 상태",
                example = "PENDING"
        )
        ApplicationStatus status,

        @Schema(
                description = "지원 일시",
                example = "2026-06-20T18:00:00"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "최근 수정 일시",
                example = "2026-06-21T12:00:00"
        )
        LocalDateTime updatedAt
) {
}
