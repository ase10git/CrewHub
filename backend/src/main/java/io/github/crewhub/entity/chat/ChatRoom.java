package io.github.crewhub.entity.chat;

import io.github.crewhub.entity.common.BaseEntity;
import io.github.crewhub.entity.gathering.Gathering;
import jakarta.persistence.*;
import lombok.*;

/**
 * 채팅방 Entity
 */
@Entity
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id", nullable = false, unique = true)
    private Gathering gathering;
}