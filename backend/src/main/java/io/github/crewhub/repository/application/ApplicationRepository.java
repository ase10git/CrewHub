package io.github.crewhub.repository.application;

import io.github.crewhub.entity.application.Application;
import io.github.crewhub.enums.application.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 지원서 Entity 관리용 Repository
 */
public interface ApplicationRepository extends JpaRepository<Application, Integer> {
    boolean existsByUserIdAndGatheringId(Integer userId, Integer gatheringId);
    boolean existsByUserIdAndGatheringIdAndStatus(
            Integer userId,
            Integer gatheringId,
            ApplicationStatus status
    );
    @Query("""
        select a
        from Application a
        join fetch a.user
        join fetch a.gathering
        where a.user.id = :userId
        order by a.createdAt desc
    """)
    List<Application> findMyApplications(@Param("userId") Integer userId);
    @Query("""
        select a
        from Application a
        join fetch a.user
        join fetch a.gathering
        where a.gathering.id = :gatheringId
        order by a.createdAt desc
    """)
    List<Application> findGatheringApplications(@Param("gatheringId") Integer gatheringId);
}
