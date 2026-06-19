package io.github.crewhub.entity.document;

import jakarta.persistence.*;
import lombok.*;

/**
 * 문서 카테고리 Map Entity
 */
@Entity
@Table(name = "document_categories_map")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DocumentCategoryMap {

    @EmbeddedId
    private DocumentCategoryMapId id;

    @MapsId("documentId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id")
    private Document document;

    @MapsId("categoryId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private DocumentCategory category;
}