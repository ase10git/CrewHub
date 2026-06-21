package io.github.crewhub.repository.application;

import io.github.crewhub.entity.application.Application;
import io.github.crewhub.enums.application.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
