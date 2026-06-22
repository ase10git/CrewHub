package io.github.crewhub.repository.document;

import io.github.crewhub.entity.document.DocumentCategoryMap;
import io.github.crewhub.entity.document.DocumentCategoryMapId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 문서 카테고리 Map Entity 관리용 Repository
 */
@Repository
public interface DocumentCategoryMapRepository extends JpaRepository<DocumentCategoryMap, DocumentCategoryMapId> {
    @Query("""
        select dcm
        from DocumentCategoryMap dcm
        join fetch dcm.category
        where dcm.document.id = :documentId
    """)
    List<DocumentCategoryMap> findDocumentCategory(@Param("documentId") Integer documentId);
    List<DocumentCategoryMap> findAllByDocumentId(Integer documentId);
    @Query("""
        select dcm
        from DocumentCategoryMap dcm
        join fetch dcm.category c
        join fetch dcm.document d
        where d.gathering.id = :gatheringId
        and c.id = :categoryId
        and d.isDeleted = false
    """)
    Page<DocumentCategoryMap> findAllByCategory(
            @Param("gatheringId") Integer gatheringId,
            @Param("categoryId") Integer categoryId,
            Pageable pageable
    );
}
