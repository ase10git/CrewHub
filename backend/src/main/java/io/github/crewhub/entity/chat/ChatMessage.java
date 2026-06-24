package io.github.crewhub.entity.chat;

import io.github.crewhub.entity.common.BaseEntity;
import io.github.crewhub.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * 채팅 메시지 Entity
 */
@Entity
@Table(name = "chat_messages")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(
            name = "content",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String content;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}