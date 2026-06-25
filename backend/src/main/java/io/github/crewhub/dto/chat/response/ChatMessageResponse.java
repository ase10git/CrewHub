package io.github.crewhub.dto.chat.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 채팅 메시지 조회 응답 데이터
 */
@Schema(description = "채팅 메시지 조회 응답")
@Builder
public record ChatMessageResponse(
        @Schema(
                description = "메시지 ID",
                example = "100"
        )
        Integer messageId,

        @Schema(
                description = "메시지 작성자 ID",
                example = "5"
        )
        Integer senderId,

        @Schema(
                description = "메시지 작성자 이름",
                example = "홍길동"
        )
        String senderName,

        @Schema(
                description = "메시지 내용",
                example = "오늘 오후 8시에 진행하겠습니다."
        )
        String content,

        @Schema(
                description = "메시지 작성 시간",
                example = "2026-06-25T10:30:00"
        )
        LocalDateTime createdAt
) {
}
