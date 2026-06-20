package io.github.crewhub.repository.gathering;

import io.github.crewhub.entity.gathering.Gathering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 모임 Entity 관리용 Repository
 */
@Repository
public interface GatheringRepository extends JpaRepository<Gathering, Integer> {
    boolean existsByGatheringName(String gatheringName);
    boolean existsByGatheringNameAndIdNot(String gatheringName, Integer gatheringId);
}
