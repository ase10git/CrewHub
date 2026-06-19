package io.github.crewhub.entity.gathering;

import io.github.crewhub.entity.common.BaseEntity;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.gathering.MemberRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 모임 회원 Entity
 */
@Entity
@Table(name = "gathering_members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GatheringMember extends BaseEntity{

    @EmbeddedId
    private GatheringMemberId id;

    @MapsId("gatheringId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id")
    private Gathering gathering;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private MemberRole role;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;
}