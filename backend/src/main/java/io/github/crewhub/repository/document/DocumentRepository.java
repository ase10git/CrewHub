package io.github.crewhub.repository.document;

import io.github.crewhub.entity.document.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 문서 Entity 관리용 Repository
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {
}
