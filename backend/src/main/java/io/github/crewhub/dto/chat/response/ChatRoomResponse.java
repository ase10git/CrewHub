package io.github.crewhub.dto.chat.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 채팅방 조회 응답 데이터
 */
@Schema(description = "채팅방 조회 응답")
@Builder
public record ChatRoomResponse(

        @Schema(
                description = "채팅방 ID",
                example = "20"
        )
        Integer roomId,

        @Schema(
                description = "연결된 모임 ID",
                example = "10"
        )
        Integer gatheringId,

        @Schema(
                description = "채팅방 생성 시간",
                example = "2026-06-25T09:00:00"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "채팅방 수정 시간",
                example = "2026-06-25T10:00:00"
        )
        LocalDateTime updatedAt
) {
}
