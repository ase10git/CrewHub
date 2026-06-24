package io.github.crewhub.service.chat;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.chat.request.CreateChatMessageRequest;
import io.github.crewhub.dto.chat.response.ChatMessageResponse;
import io.github.crewhub.dto.chat.response.ChatRoomResponse;
import io.github.crewhub.dto.chat.response.CreateChatMessageResponse;
import io.github.crewhub.dto.common.PageResponse;
import io.github.crewhub.entity.chat.ChatMessage;
import io.github.crewhub.entity.chat.ChatRoom;
import io.github.crewhub.entity.gathering.Gathering;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.repository.chat.ChatMessageRepository;
import io.github.crewhub.repository.chat.ChatRoomRepository;
import io.github.crewhub.repository.gathering.GatheringMemberRepository;
import io.github.crewhub.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 채팅 서비스
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final GatheringMemberRepository memberRepository;
    private final UserRepository userRepository;

    private final SimpMessagingTemplate messagingTemplate;

    public ChatRoomResponse getRoom(Integer userId, Integer roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.CHATROOM_NOT_FOUND)
                );

        validateActiveGathering(room.getGathering());

        validateMember(userId, room.getGathering().getId());

        return ChatRoomResponse.builder()
                .roomId(roomId)
                .gatheringId(room.getGathering().getId())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build();
    }

    private void validateMember(Integer userId, Integer gatheringId) {
        boolean exists = memberRepository.existsByGatheringIdAndUserId(gatheringId, userId);

        if (!exists) {
            throw new BusinessException(ErrorCode.GATHERING_MEMBER_ONLY);
        }
    }

    private ChatRoom findRoom(Integer roomId) {
        return chatRoomRepository.findById(roomId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.CHATROOM_NOT_FOUND)
                );
    }

    private ChatRoom findRoomByGathering(Integer gatheringId) {
        return chatRoomRepository.findByGatheringId(gatheringId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.CHATROOM_NOT_FOUND)
                );
    }

    @Transactional
    public CreateChatMessageResponse sendSocketMessage(
            Integer userId,
            Integer gatheringId,
            CreateChatMessageRequest request
    ) {
        User user = getUser(userId);

        ChatRoom room = findRoomByGathering(gatheringId);

        validateActiveGathering(room.getGathering());

        validateMember(userId, room.getGathering().getId());

        ChatMessage message = ChatMessage.builder()
                .chatRoom(room)
                .user(user)
                .content(request.content())
                .build();

        ChatMessage saved = chatMessageRepository.save(message);

        CreateChatMessageResponse chatMessageSocketResponse =
                CreateChatMessageResponse.builder()
                        .messageId(saved.getId())
                        .roomId(room.getId())
                        .senderId(user.getId())
                        .senderName(user.getUsername())
                        .content(saved.getContent())
                        .createdAt(saved.getCreatedAt())
                        .build();

        messagingTemplate.convertAndSend(
                "/topic/chat/" + gatheringId,
                chatMessageSocketResponse
        );

        return chatMessageSocketResponse;
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );
    }

    private void validateActiveGathering(Gathering gathering) {
        if (Boolean.TRUE.equals(gathering.getIsDeleted())) {
            throw new BusinessException(ErrorCode.GATHERING_NOT_FOUND);
        }
    }

    public PageResponse<ChatMessageResponse> getMessages(
            Integer userId,
            Integer roomId,
            int page,
            int size
    ) {
        ChatRoom room = findRoom(roomId);

        validateActiveGathering(room.getGathering());

        validateMember(userId, room.getGathering().getId());

        Pageable pageable = PageRequest.of(page, size);

        Page<ChatMessage> messages = chatMessageRepository.findMessages(
                roomId,
                pageable
        );

        return new PageResponse<>(
                messages.stream()
                        .map(message ->
                                ChatMessageResponse.builder()
                                        .messageId(message.getId())
                                        .senderId(message.getUser().getId())
                                        .senderName(message.getUser().getUsername())
                                        .content(message.getContent())
                                        .createdAt(message.getCreatedAt())
                                        .build()
                        )
                        .toList(),
                messages.getNumber(),
                messages.getSize(),
                messages.getTotalElements(),
                messages.getTotalPages(),
                messages.hasNext()
        );
    }

}
