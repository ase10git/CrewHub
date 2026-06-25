package io.github.crewhub.controller.chat;


import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.chat.response.ChatMessageResponse;
import io.github.crewhub.dto.chat.response.ChatRoomResponse;
import io.github.crewhub.dto.common.PageResponse;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.service.chat.ChatService;
import io.github.crewhub.swagger.annotation.chat.GetChatroomApi;
import io.github.crewhub.swagger.annotation.chat.GetMessagesApi;
import io.github.crewhub.swagger.response.forbidden.GatheringMemberOnlyResponse;
import io.github.crewhub.swagger.response.notfound.GatheringOrChatroomNotFoundResponse;
import io.github.crewhub.swagger.response.unauthorized.UnauthorizedResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 채팅 정보 요청 처리
 */
@Tag(
        name = "Chat",
        description = "채팅방 및 메시지 API"
)
@UnauthorizedResponse
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetChatroomApi
    @GatheringOrChatroomNotFoundResponse
    @GatheringMemberOnlyResponse
    @GetMapping("/room/{roomId}")
    public ApiResponse<ChatRoomResponse> getRoom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer roomId
    ) {
        return ApiResponse.success(
                chatService.getRoom(userDetails.getUserId(), roomId)
        );
    }

    @GetMessagesApi
    @GatheringOrChatroomNotFoundResponse
    @GatheringMemberOnlyResponse
    @GetMapping("/room/{roomId}/messages")
    public ApiResponse<PageResponse<ChatMessageResponse>> getMessages(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer roomId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size
    ) {
        return ApiResponse.success(
                chatService.getMessages(
                        userDetails.getUserId(),
                        roomId,
                        page,
                        size
                )
        );
    }
}
