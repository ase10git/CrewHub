package io.github.crewhub.dto.chat.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 채팅 메시지 생성 응답 데이터
 */
@Schema(description = "채팅 메시지 생성 응답")
@Builder
public record CreateChatMessageResponse(
        @Schema(
                description = "생성된 메시지 ID",
                example = "101"
        )
        Integer messageId,

        @Schema(
                description = "채팅방 ID",
                example = "20"
        )
        Integer roomId,

        @Schema(
                description = "작성자 ID",
                example = "5"
        )
        Integer senderId,

        @Schema(
                description = "작성자 이름",
                example = "홍길동"
        )
        String senderName,

        @Schema(
                description = "메시지 내용",
                example = "확인했습니다."
        )
        String content,

        @Schema(
                description = "메시지 생성 시간",
                example = "2026-06-25T10:30:00"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "메시지 수정 시간",
                example = "2026-06-25T10:35:00"
        )
        LocalDateTime updatedAt
) {
}
