package io.github.crewhub.repository.chat;

import io.github.crewhub.entity.chat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 채팅 메시지 Entity 관리용 Repository
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    @Query("""
        select cm
        from ChatMessage cm
        join fetch cm.user
        where cm.chatRoom.id = :roomId
        order by cm.createdAt asc
    """)
    Page<ChatMessage> findMessages(
            @Param("roomId") Integer roomId,
            Pageable pageable
    );
}
