package io.github.crewhub.repository.document;

import io.github.crewhub.entity.document.DocumentCategoryMap;
import io.github.crewhub.entity.document.DocumentCategoryMapId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 문서 카테고리 Map Entity 관리용 Repository
 */
@Repository
public interface DocumentCategoryMapRepository extends JpaRepository<DocumentCategoryMap, DocumentCategoryMapId> {
}
