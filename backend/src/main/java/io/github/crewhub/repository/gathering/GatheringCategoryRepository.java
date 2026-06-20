package io.github.crewhub.repository.gathering;

import io.github.crewhub.entity.gathering.GatheringCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 모임 카테고리 Entity 관리용 Repository
 */
@Repository
public interface GatheringCategoryRepository extends JpaRepository<GatheringCategory, Integer> {
    Optional<GatheringCategory> findById(Integer categoryId);
}
