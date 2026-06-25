package io.github.crewhub.enums.application;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 지원 상태
 */
@Schema(
        description = """
                지원 처리 상태
                
                * PENDING - 지원 대기
                * APPROVED - 지원 승인
                * REJECTED - 지원 거절
                * CANCELLED - 지원 취소
                """
)
public enum ApplicationStatus {
    @Schema(description = "지원 대기 상태")
    PENDING,

    @Schema(description = "지원 승인 상태")
    APPROVED,

    @Schema(description = "지원 거절 상태")
    REJECTED,

    @Schema(description = "지원 취소 상태")
    CANCELLED
}