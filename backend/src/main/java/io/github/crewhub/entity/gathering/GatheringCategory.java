package io.github.crewhub.entity.gathering;

import io.github.crewhub.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 모임 카테고리 Entity
 */
@Entity
@Table(name = "gathering_categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GatheringCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "key", nullable = false, length = 50)
    private String key;

    @Column(name = "label", nullable = false, length = 50)
    private String label;
}