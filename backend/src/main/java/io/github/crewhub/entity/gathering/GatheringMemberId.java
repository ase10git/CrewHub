package io.github.crewhub.entity.gathering;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * 모임 회원 복합 키
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GatheringMemberId implements Serializable {
    @Column(name = "gathering_id")
    private Integer gatheringId;

    @Column(name = "user_id")
    private Integer userId;
}
