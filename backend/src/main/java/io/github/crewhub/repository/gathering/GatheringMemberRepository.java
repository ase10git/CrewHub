package io.github.crewhub.repository.gathering;

import io.github.crewhub.entity.gathering.GatheringMember;
import io.github.crewhub.entity.gathering.GatheringMemberId;
import io.github.crewhub.enums.gathering.MemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 모임 가입자 Entity 관리용 Repository
 */
@Repository
public interface GatheringMemberRepository extends JpaRepository<GatheringMember, GatheringMemberId> {
    boolean existsByGatheringIdAndUserId(Integer gatheringId, Integer userId);
    Optional<GatheringMember> findById(GatheringMemberId id);
    Page<GatheringMember> findByUserId(Integer userId, Pageable pageable);
    @Query("""
        select gm
        from GatheringMember gm
        join fetch gm.user
        where gm.gathering.id = :gatheringId
        and (:role is null or gm.role = :role)
    """)
    Page<GatheringMember> findMembers(
            @Param("gatheringId") Integer gatheringId,
            @Param("role") MemberRole role,
            Pageable pageable
    );
    long countByGatheringId(Integer gatheringId);
}
