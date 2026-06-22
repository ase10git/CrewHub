package io.github.crewhub.dto.gathering.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 관리자 변경 응답 데이터
 */
@Builder
public record TransferManagerResponse(
        Integer gatheringId,
        Integer previousManagerId,
        Integer newManagerId,
        String newManagerName,
        LocalDateTime transferredAt
) {
}
