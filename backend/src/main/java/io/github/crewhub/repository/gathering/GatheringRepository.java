package io.github.crewhub.repository.gathering;

import io.github.crewhub.entity.gathering.Gathering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 모임 Entity 관리용 Repository
 */
@Repository
public interface GatheringRepository extends JpaRepository<Gathering, Integer> {
    @Query(
    value = """
        select g
        from Gathering g
        join fetch g.category
        join fetch g.manager
        where g.isDeleted = false
    """,
    countQuery = """
        select count(g)
        from Gathering g
        where g.isDeleted = false
    """
    )
    Optional<Gathering> findDetailById(@Param("gatheringId") Integer gatheringId);
    @Query("""
        select g
        from Gathering g
        join fetch g.category
        join fetch g.manager
        where g.isDeleted = false
    """)
    Page<Gathering> findAllActive(Pageable pageable);
    @Query("""
        select g
        from Gathering g
        join fetch g.manager
        join fetch g.category
        where g.id = :gatheringId
        and g.isDeleted = false
    """)
    Optional<Gathering> findActiveById(@Param("gatheringId") Integer gatheringId);
    @Query("""
        select g
        from Gathering g
        where g.isDeleted = false
        and lower(g.gatheringName)
        like lower(concat('%', :keyword, '%'))
    """)
    Page<Gathering> findByGatheringName( @Param("keyword") String keyword, Pageable pageable);
    @Query("""
        select g
        from Gathering g
        where g.isDeleted = false
        and g.category.id = :categoryId
    """)
    Page<Gathering> findByCategoryId(@Param("categoryId") Integer categoryId, Pageable pageable);
    boolean existsByGatheringName(String gatheringName);
    boolean existsByGatheringNameAndIdNot(String gatheringName, Integer gatheringId);
}
