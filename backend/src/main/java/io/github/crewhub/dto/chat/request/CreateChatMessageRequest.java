package io.github.crewhub.dto.chat.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * 채팅 메시지 생성 요청 데이터
 */
@Schema(description = "채팅 메시지 생성 요청")
@Builder
public record CreateChatMessageRequest(
        @Schema(
                description = "전송할 메시지 내용",
                example = "안녕하세요. 오늘 스터디 일정 확인 부탁드립니다.",
                maxLength = 1000
        )
        @NotBlank @Size(max = 1000) String content
) {
}
