package io.github.crewhub.dto.chat.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * 채팅 메시지 생성 요청 데이터
 */
@Builder
public record CreateChatMessageRequest(
        @NotBlank String content
) {
}
