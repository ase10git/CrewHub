package io.github.crewhub.entity.gathering;

import io.github.crewhub.entity.common.BaseEntity;
import io.github.crewhub.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.*;

/**
 * 모임 Entity
 */
@Entity
@Table(name = "gatherings")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Gathering extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(
            name = "gathering_name",
            nullable = false,
            length = 100,
            unique = true
    )
    private String gatheringName;

    @Column(
            name = "description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "category_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_gathering_category")
    )
    private GatheringCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "manager_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_gathering_manager")
    )
    private User manager;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public void updateGathering(
            String gatheringName,
            String description
    ) {
        this.gatheringName = gatheringName;
        this.description = description;
    }
}
