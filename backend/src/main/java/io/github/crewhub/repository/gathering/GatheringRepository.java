package io.github.crewhub.repository.gathering;

import io.github.crewhub.entity.gathering.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 모임 Entity 관리용 Repository
 */
@Repository
public interface GatheringRepository extends JpaRepository<Gathering, Integer> {
    @Query("""
        select g
        from Gathering g
        join fetch g.category
        join fetch g.manager
        where g.id = :gatheringId
    """)
    Optional<Gathering> findDetailById(
            @Param("gatheringId") Integer gatheringId
    );
    @Query("""
        select g
        from Gathering g
        join fetch g.category
        join fetch g.manager
        where g.isDeleted = false
    """)
    List<Gathering> findAllActive();
    List<Gathering> findByGatheringNameContainingIgnoreCase(String keyword);
    List<Gathering> findByCategoryId(Integer categoryId);
    boolean existsByGatheringName(String gatheringName);
    boolean existsByGatheringNameAndIdNot(String gatheringName, Integer gatheringId);
}
