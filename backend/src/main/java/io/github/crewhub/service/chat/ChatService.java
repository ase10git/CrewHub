package io.github.crewhub.service.chat;

import io.github.crewhub.repository.chat.ChatMessageRepository;
import io.github.crewhub.repository.chat.ChatRoomRepository;
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
}
