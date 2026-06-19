package io.github.crewhub.entity.document;

import io.github.crewhub.entity.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * 문서 카테고리 Entity
 */
@Entity
@Table(name = "document_categories")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocumentCategory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "key", nullable = false, length = 50)
    private String key;

    @Column(name = "label", nullable = false, length = 50)
    private String label;
}