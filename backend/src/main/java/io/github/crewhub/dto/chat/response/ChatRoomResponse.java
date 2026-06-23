package io.github.crewhub.dto.chat.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 채팅방 조회 응답 데이터
 */
@Builder
public record ChatRoomResponse(
        Integer roomId,
        Integer gatheringId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
