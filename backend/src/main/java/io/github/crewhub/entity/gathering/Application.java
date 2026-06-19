package io.github.crewhub.entity.gathering;

import io.github.crewhub.entity.common.BaseEntity;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.gathering.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

/**
 * 모임 지원 Entity
 */
@Entity
@Table(
        name = "applications",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_application_gathering_user",
                        columnNames = {"gathering_id", "user_id"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id", nullable = false)
    private Gathering gathering;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ApplicationStatus status;
}