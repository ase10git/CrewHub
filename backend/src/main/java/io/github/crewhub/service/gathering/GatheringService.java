package io.github.crewhub.service.gathering;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.gathering.request.CreateGatheringRequest;
import io.github.crewhub.dto.gathering.response.CreateGatheringResponse;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
