package io.github.crewhub.dto.chat.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * 채팅 메시지 생성 요청 데이터
 */
@Builder
public record CreateChatMessageRequest(
        @NotBlank @Size(max = 1000) String content
) {
}
