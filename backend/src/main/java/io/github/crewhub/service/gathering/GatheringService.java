package io.github.crewhub.service.gathering;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.common.PageResponse;
import io.github.crewhub.dto.gathering.request.CreateGatheringRequest;
import io.github.crewhub.dto.gathering.request.UpdateGatheringRequest;
import io.github.crewhub.dto.gathering.response.*;
import io.github.crewhub.entity.gathering.Gathering;
import io.github.crewhub.entity.gathering.GatheringCategory;
import io.github.crewhub.entity.gathering.GatheringMember;
import io.github.crewhub.entity.gathering.GatheringMemberId;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.enums.gathering.MemberRole;
import io.github.crewhub.repository.gathering.GatheringCategoryRepository;
import io.github.crewhub.repository.gathering.GatheringMemberRepository;
import io.github.crewhub.repository.gathering.GatheringRepository;
import io.github.crewhub.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 모임 서비스
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GatheringService {
    private final GatheringRepository gatheringRepository;
    private final GatheringCategoryRepository categoryRepository;
    private final GatheringMemberRepository memberRepository;
    private final UserRepository userRepository;

    public List<GatheringSummaryResponse> getGatherings() {

        return gatheringRepository.findAllActive()
                .stream()
                .map(this::toSummaryResponse)
                .toList();
    }

    public GatheringDetailResponse getGathering(Integer gatheringId) {
        Gathering gathering =
                gatheringRepository.findDetailById(gatheringId)
                        .orElseThrow(
                                () -> new BusinessException(
                                        ErrorCode.GATHERING_NOT_FOUND
                                )
                        );

        return GatheringDetailResponse.builder()
                .gatheringId(gathering.getId())
                .gatheringName(gathering.getGatheringName())
                .description(gathering.getDescription())
                .categoryLabel(gathering.getCategory().getLabel())
                .managerId(gathering.getManager().getId())
                .managerName(gathering.getManager().getUsername())
                .build();
    }

    public List<GatheringSummaryResponse> searchByName(String keyword) {

        return gatheringRepository
                .findByGatheringNameContainingIgnoreCase(keyword)
                .stream()
                .map(this::toSummaryResponse)
                .toList();
    }

    public List<GatheringSummaryResponse> searchByCategory(Integer categoryId) {

        return gatheringRepository
                .findByCategoryId(categoryId)
                .stream()
                .map(this::toSummaryResponse)
                .toList();
    }

    private GatheringSummaryResponse toSummaryResponse(Gathering gathering) {

        return GatheringSummaryResponse.builder()
                .gatheringId(gathering.getId())
                .gatheringName(gathering.getGatheringName())
                .categoryLabel(
                        gathering.getCategory().getLabel()
                )
                .managerName(
                        gathering.getManager().getUsername()
                )
                .build();
    }

    @Transactional
    public CreateGatheringResponse create(Integer userId, CreateGatheringRequest request) {
        validateDuplicatedGatheringName(request.gatheringName());

        GatheringCategory category = getCategory(request.categoryId());

        User manager = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );

        Gathering gathering = Gathering.builder()
                .gatheringName(request.gatheringName())
                .category(category)
                .manager(manager)
                .description(request.description())
                .build();

        Gathering savedGathering = gatheringRepository.save(gathering);

        registerManager(savedGathering, manager);

        return CreateGatheringResponse.builder()
                .gatheringId(savedGathering.getId())
                .gatheringName(savedGathering.getGatheringName())
                .build();
    }

    private GatheringCategory getCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(
                        () -> new BusinessException(
                                ErrorCode.CATEGORY_NOT_FOUND
                        )
                );
    }

    private void validateDuplicatedGatheringName(String gatheringName) {
        if (gatheringRepository.existsByGatheringName(gatheringName)) {
            throw new BusinessException(ErrorCode.DUPLICATE_GATHERING_NAME);
        }
    }

    private void registerManager(Gathering gathering, User manager) {
        GatheringMember member = GatheringMember.builder()
                .id(new GatheringMemberId(gathering.getId(), manager.getId()))
                .gathering(gathering)
                .user(manager)
                .role(MemberRole.MANAGER)
                .build();

        memberRepository.save(member);
    }

    @Transactional
    public UpdateGatheringResponse update(
            Integer userId,
            Integer gatheringId,
            UpdateGatheringRequest request
    ) {
        Gathering gathering = findGathering(gatheringId);

        validateManager(userId, gathering);

        validateDuplicateName(request.gatheringName(), gatheringId);

        gathering.updateGathering(request.gatheringName(), request.description());

        return UpdateGatheringResponse.builder()
                .gatheringId(gathering.getId())
                .gatheringName(gathering.getGatheringName())
                .description(gathering.getDescription())
                .build();
    }

    private Gathering findGathering(Integer gatheringId) {
        return gatheringRepository.findDetailById(gatheringId)
                .orElseThrow(
                        () -> new BusinessException(
                                ErrorCode.GATHERING_NOT_FOUND
                        )
                );
    }

    private void validateDuplicateName(
            String gatheringName,
            Integer gatheringId
    ) {
        if (gatheringRepository
                .existsByGatheringNameAndIdNot(gatheringName, gatheringId)) {
            throw new BusinessException(
                    ErrorCode.DUPLICATE_GATHERING_NAME
            );
        }
    }

    private void validateManager(Integer userId, Gathering gathering) {
        if (!gathering.getManager()
                .getId()
                .equals(userId)) {

            throw new BusinessException(ErrorCode.GATHERING_MANAGER_ONLY);
        }
    }

    @Transactional
    public void delete(Integer userId, Integer gatheringId) {
        Gathering gathering = findGathering(gatheringId);

        validateManager(userId, gathering);

        gathering.delete();
    }

    /**
     * 모임 회원 관리
     */
    
    
    public PageResponse<GatheringMemberResponse> getMembers(
            Integer gatheringId,
            MemberRole role,
            int page,
            int size
    ) {

        getGathering(gatheringId);

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
        GatheringMemberId id = new GatheringMemberId(gatheringId, userId);

        GatheringMember member =
                memberRepository.findById(id)
                        .orElseThrow(
                                () -> new BusinessException(ErrorCode.GATHERING_MEMBER_NOT_FOUND)
                        );

        validateManagerLeave(member);

        memberRepository.delete(member);

        deleteGatheringIfEmpty(gatheringId);

        return LeaveGatheringResponse.builder()
                .gatheringId(gatheringId)
                .userId(userId)
                .message("모임에서 탈퇴했습니다.")
                .leftAt(LocalDateTime.now())
                .build();
    }

    private void validateManagerLeave(GatheringMember member) {
        if (member.getRole() != MemberRole.MANAGER) {
            return;
        }

        long managerCount = memberRepository.countByGatheringIdAndRole(
                member.getGathering().getId(),
                MemberRole.MANAGER
        );

        long memberCount = memberRepository.countByGatheringId(member.getGathering().getId());

        if (managerCount == 1 && memberCount > 1) {
            throw new BusinessException(ErrorCode.MANAGER_CANNOT_LEAVE);
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
}
