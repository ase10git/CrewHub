package io.github.crewhub.repository.document;

import io.github.crewhub.entity.document.DocumentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 문서 카테고리 Entity 관리용 Repository
 */
@Repository
public interface DocumentCategoryRepository extends JpaRepository<DocumentCategory, Integer> {
}
