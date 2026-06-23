package io.github.crewhub.repository.chat;

import io.github.crewhub.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 채팅방 Entity 관리용 Repository
 */
@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    @Query("""
        select cr
        from ChatRoom cr
        join fetch cr.gathering
        where cr.id = :roomId
    """)
    Optional<ChatRoom> findById(@Param("roomId") Integer roomId);
    Optional<ChatRoom> findByGatheringId(Integer gatheringId);
}
