package io.github.crewhub.repository.chat;

import io.github.crewhub.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 채팅방 Entity 관리용 Repository
 */
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
}
