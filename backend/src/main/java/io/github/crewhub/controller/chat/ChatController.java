package io.github.crewhub.controller.chat;


import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.chat.request.CreateChatMessageRequest;
import io.github.crewhub.dto.chat.response.ChatRoomResponse;
import io.github.crewhub.dto.chat.response.CreateChatMessageResponse;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 채팅 정보 요청 처리
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/room/{roomId}")
    public ApiResponse<ChatRoomResponse> getRoom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer roomId
    ) {
        return ApiResponse.success(
                chatService.getRoom(userDetails.getUserId(), roomId)
        );
    }

    @PostMapping("/room/{roomId}/message")
    public ApiResponse<CreateChatMessageResponse> sendMessage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer roomId,
            @RequestBody CreateChatMessageRequest request
            ) {
        return ApiResponse.success(
                chatService.sendMessage(userDetails.getUserId(), roomId, request)
        );
    }
}
