package io.github.crewhub.controller.chat;

import io.github.crewhub.dto.chat.request.CreateChatMessageRequest;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * WebSocket 요청 처리
 */
@Controller
@RequiredArgsConstructor
public class ChatSocketController {
    private final ChatService chatService;

    @MessageMapping("/chat/{gatheringId}/send")
    public void send(
            Principal principal,
            CreateChatMessageRequest request,
            @DestinationVariable Integer gatheringId
    ) {
        Authentication authentication = (Authentication) principal;

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        chatService.sendSocketMessage(
                userDetails.getUserId(),
                gatheringId,
                request
                );
    }
}
