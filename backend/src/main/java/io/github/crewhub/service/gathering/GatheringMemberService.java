package io.github.crewhub.service.gathering;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.common.PageResponse;
import io.github.crewhub.dto.gathering.response.*;
import io.github.crewhub.entity.gathering.Gathering;
import io.github.crewhub.entity.gathering.GatheringMember;
import io.github.crewhub.entity.gathering.GatheringMemberId;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.enums.gathering.MemberRole;
import io.github.crewhub.repository.gathering.GatheringMemberRepository;
import io.github.crewhub.repository.gathering.GatheringRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 모임 회원 서비스
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GatheringMemberService {
    private final GatheringRepository gatheringRepository;
    private final GatheringMemberRepository memberRepository;

    private Gathering findGathering(Integer gatheringId) {
        return gatheringRepository.findDetailById(gatheringId)
                .orElseThrow(
                        () -> new BusinessException(
                                ErrorCode.GATHERING_NOT_FOUND
                        )
                );
    }

    private void validateManager(Integer userId, Gathering gathering) {
        if (!gathering.getManager()
                .getId()
                .equals(userId)) {

            throw new BusinessException(ErrorCode.GATHERING_MANAGER_ONLY);
        }
    }

    public PageResponse<GatheringMemberResponse> getMembers(
            Integer gatheringId,
            MemberRole role,
            int page,
            int size
    ) {

        findGathering(gatheringId);

        Pageable pageable = PageRequest.of(page, size);

        Page<GatheringMember> members =
                memberRepository.findMembers(gatheringId, role, pageable);

        return new PageResponse<>(
                members.stream()
                        .map(member ->
                                GatheringMemberResponse.builder()
                                        .userId(member.getUser().getId())
                                        .username(member.getUser().getUsername())
                                        .role(member.getRole())
                                        .createdAt(member.getCreatedAt())
                                        .updatedAt(member.getUpdatedAt())
                                        .build()
                        )
                        .toList(),
                members.getNumber(),
                members.getSize(),
                members.getTotalElements(),
                members.getTotalPages(),
                members.hasNext()
        );
    }

    @Transactional
    public LeaveGatheringResponse leave(Integer userId, Integer gatheringId) {
        GatheringMember member = getMember(gatheringId, userId);

        validateLastManagerRemoval(member, ErrorCode.MANAGER_CANNOT_LEAVE);

        memberRepository.delete(member);

        deleteGatheringIfEmpty(gatheringId);

        return LeaveGatheringResponse.builder()
                .gatheringId(gatheringId)
                .userId(userId)
                .message("모임에서 탈퇴했습니다.")
                .leftAt(LocalDateTime.now())
                .build();
    }

    private GatheringMember getMember(Integer gatheringId, Integer userId) {
        return memberRepository.findById(
                        new GatheringMemberId(gatheringId, userId)
                )
                .orElseThrow(
                        () -> new BusinessException(
                                ErrorCode.GATHERING_MEMBER_NOT_FOUND
                        )
                );
    }

    private void validateLastManagerRemoval(GatheringMember member, ErrorCode errorCode) {
        if (member.getRole() != MemberRole.MANAGER) {
            return;
        }

        long managerCount = memberRepository.countByGatheringIdAndRole(
                member.getGathering().getId(),
                MemberRole.MANAGER
        );

        long memberCount = memberRepository.countByGatheringId(member.getGathering().getId());

        if (managerCount == 1 && memberCount > 1) {
            throw new BusinessException(errorCode);
        }
    }

    private void deleteGatheringIfEmpty(Integer gatheringId) {
        long memberCount =
                memberRepository.countByGatheringId(gatheringId);

        if (memberCount == 0) {
            Gathering gathering =
                    gatheringRepository.findById(gatheringId)
                            .orElseThrow(
                                    () -> new BusinessException(ErrorCode.GATHERING_NOT_FOUND)
                            );

            gathering.delete();
        }
    }

    @Transactional
    public KickMemberResponse kickMember(
            Integer managerId,
            Integer gatheringId,
            Integer targetUserId
    ) {
        Gathering gathering = findGathering(gatheringId);

        validateManager(managerId, gathering);

        GatheringMember targetMember = getMember(gatheringId, targetUserId);

        validateKick(managerId, targetMember);

        memberRepository.delete(targetMember);

        deleteGatheringIfEmpty(gatheringId);

        return KickMemberResponse.builder()
                .gatheringId(gatheringId)
                .userId(targetUserId)
                .username(targetMember.getUser().getUsername())
                .build();
    }

    private void validateKick(Integer managerId, GatheringMember targetMember) {
        if (targetMember.getUser()
                .getId()
                .equals(managerId)) {
            throw new BusinessException(ErrorCode.CANNOT_KICK_SELF);
        }

        validateLastManagerRemoval(targetMember, ErrorCode.LAST_MANAGER_CANNOT_BE_REMOVED);
    }

    @Transactional
    public TransferManagerResponse transferManager(
            Integer managerId,
            Integer gatheringId,
            Integer targetUserId
    ) {
        Gathering gathering = findGathering(gatheringId);

        validateManager(managerId, gathering);
        validateTransfer(managerId, targetUserId);

        GatheringMember currentManager = getMember(gatheringId, managerId);
        GatheringMember newManager = getMember(gatheringId, targetUserId);

        validateTarget(newManager);

        currentManager.changeRole(MemberRole.MEMBER);

        newManager.changeRole(MemberRole.MANAGER);

        gathering.changeManager(newManager.getUser());

        return TransferManagerResponse.builder()
                .gatheringId(gatheringId)
                .previousManagerId(managerId)
                .newManagerId(targetUserId)
                .newManagerName(newManager.getUser().getUsername())
                .transferredAt(LocalDateTime.now())
                .build();
    }

    private void validateTransfer(Integer managerId, Integer targetUserId) {
        if (managerId.equals(targetUserId)) {
            throw new BusinessException(ErrorCode.CANNOT_TRANSFER_TO_SELF);
        }
    }

    private void validateTarget(GatheringMember newManager) {
        if (newManager.getRole().equals(MemberRole.MANAGER)) {
            throw new BusinessException(ErrorCode.ALREADY_MANAGER);
        }
    }

    public PageResponse<MyGatheringResponse> getMyGatherings(
            Integer userId,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<GatheringMember> gatherings =
                memberRepository.findMyGatherings(userId, pageable);

        return new PageResponse<>(
                gatherings.stream()
                        .map(member ->
                                MyGatheringResponse.builder()
                                        .gatheringId(member.getGathering().getId())
                                        .gatheringName(member.getGathering().getGatheringName())
                                        .categoryLabel(
                                                member.getGathering()
                                                        .getCategory()
                                                        .getLabel()
                                        )
                                        .role(member.getRole())
                                        .joinedAt(member.getCreatedAt())
                                        .build()
                        )
                        .toList(),
                gatherings.getNumber(),
                gatherings.getSize(),
                gatherings.getTotalElements(),
                gatherings.getTotalPages(),
                gatherings.hasNext()
        );
    }

    public GatheringMemberCountResponse getMemberCount(Integer gatheringId) {
        findGathering(gatheringId);

        long memberCount = memberRepository.countByGatheringId(gatheringId);

        return GatheringMemberCountResponse.builder()
                .gatheringId(gatheringId)
                .memberCount(memberCount)
                .build();
    }
}
