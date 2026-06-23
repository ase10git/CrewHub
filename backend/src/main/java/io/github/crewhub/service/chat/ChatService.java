package io.github.crewhub.service.chat;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.chat.response.ChatRoomResponse;
import io.github.crewhub.entity.chat.ChatRoom;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.repository.chat.ChatMessageRepository;
import io.github.crewhub.repository.chat.ChatRoomRepository;
import io.github.crewhub.repository.gathering.GatheringMemberRepository;
import lombok.RequiredArgsConstructor;
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

    public ChatRoomResponse getRoom(Integer userId, Integer roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.CHATROOM_NOT_FOUND)
                );

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
}
