package io.github.crewhub.repository.document;

import io.github.crewhub.entity.document.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 문서 Entity 관리용 Repository
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
    Page<Document> findByGatheringIdAndIsDeletedFalse(Integer gatheringId, Pageable pageable);
    @Query(
    value = """
        select d
        from Document d
        join fetch d.writer
        join fetch d.gathering
        where d.isDeleted = false
        and d.id = :documentId
    """
    )
    Optional<Document> findDetailById(@Param("documentId") Integer documentId);
    @Query("""
        select d
        from Document d
        where d.isDeleted = false
        and d.gathering.id = :gatheringId
        and lower(d.title)
        like lower(concat('%', :keyword, '%'))
    """)
    Page<Document> findByTitle(
            @Param("gatheringId") Integer gatheringId,
            @Param("keyword") String keyword,
            Pageable pageable
    );
    Page<Document> findByWriterId(Integer writerId, Pageable pageable);
    long countByGatheringIdAndIsDeletedFalse(Integer gatheringId);
    long countByWriterIdAndIsDeletedFalse(Integer writerId);
}
