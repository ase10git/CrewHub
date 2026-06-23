package io.github.crewhub.repository.chat;

import io.github.crewhub.entity.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 채팅 메시지 Entity 관리용 Repository
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
}
