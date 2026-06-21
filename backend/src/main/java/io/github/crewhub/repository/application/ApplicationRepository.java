package io.github.crewhub.repository.application;

import io.github.crewhub.entity.application.Application;
import io.github.crewhub.enums.application.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 지원서 Entity 관리용 Repository
 */
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    boolean existsByUserIdAndGatheringId(Integer userId, Integer gatheringId);
    Optional<Application> findByIdAndUserId(Integer applicationId, Integer userId);
    @Query(
    value = """
        select a
        from Application a
        join fetch a.gathering
        where a.user.id = :userId
        order by a.createdAt desc
    """,
        countQuery = """
        select count(a)
        from Application a
        where a.user.id = :userId
    """
    )
    Page<Application> findMyApplications(
            @Param("userId") Integer userId,
            Pageable pageable
    );
    @Query("""
        select a
        from Application a
        join fetch a.user
        join fetch a.gathering
        where a.gathering.id = :gatheringId
        and (:status is NULL or a.status = :status)
        order by a.createdAt desc
    """)
    Page<Application> findGatheringApplications(
            @Param("gatheringId") Integer gatheringId,
            @Param("status") ApplicationStatus status,
            Pageable pageable
    );
}
