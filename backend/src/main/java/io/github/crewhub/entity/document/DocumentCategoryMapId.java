package io.github.crewhub.entity.document;

import jakarta.persistence.*;
import lombok.*;
import java.io.Serializable;

/**
 * 문서 카테고리 복합 키
 */
@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DocumentCategoryMapId implements Serializable {

    @Column(name = "document_id")
    private Integer documentId;

    @Column(name = "category_id")
    private Integer categoryId;
}