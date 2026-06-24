package io.github.crewhub.dto.chat.response;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 채팅 메시지 조회 응답 데이터
 */
@Builder
public record ChatMessageResponse(
        Integer messageId,
        Integer senderId,
        String senderName,
        String content,
        LocalDateTime createdAt
) {
}
