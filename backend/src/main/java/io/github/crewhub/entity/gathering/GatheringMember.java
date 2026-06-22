package io.github.crewhub.entity.gathering;

import io.github.crewhub.entity.common.BaseEntity;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.gathering.MemberRole;
import jakarta.persistence.*;
import lombok.*;

/**
 * 모임 회원 Entity
 */
@Entity
@Table(name = "gathering_members")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
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

    public void changeRole(MemberRole role) {
        this.role = role;
    }
}