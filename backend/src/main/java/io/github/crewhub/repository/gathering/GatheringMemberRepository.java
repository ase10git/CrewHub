package io.github.crewhub.repository.gathering;

import io.github.crewhub.entity.gathering.GatheringMember;
import io.github.crewhub.entity.gathering.GatheringMemberId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * 모임 가입자 Entity 관리용 Repository
 */
@Repository
public interface GatheringMemberRepository extends JpaRepository<GatheringMember, GatheringMemberId> {
    boolean existsByGatheringIdAndUserId(Integer gatheringId, Integer userId);
    @Query(
    value = """
        select gm
        from GatheringMember gm
        join fetch gm.user
        where gm.gathering.id = :gatheringId
    """,
    countQuery = """
        select count(gm)
        from GatheringMember gm
        where gm.gathering.id = :gatheringId
    """
    )
    Page<GatheringMember> findByGatheringId(Integer gatheringId, Pageable pageable);
}
