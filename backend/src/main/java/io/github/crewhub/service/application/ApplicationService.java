package io.github.crewhub.service.application;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.application.request.CreateApplicationRequest;
import io.github.crewhub.dto.application.response.CreateApplicationResponse;
import io.github.crewhub.dto.application.response.MyApplicationResponse;
import io.github.crewhub.entity.application.Application;
import io.github.crewhub.entity.gathering.Gathering;
import io.github.crewhub.entity.gathering.GatheringMemberId;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.application.ApplicationStatus;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.repository.application.ApplicationRepository;
import io.github.crewhub.repository.gathering.GatheringMemberRepository;
import io.github.crewhub.repository.gathering.GatheringRepository;
import io.github.crewhub.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 지원서 서비스
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ApplicationService {
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final GatheringRepository gatheringRepository;
    private final GatheringMemberRepository memberRepository;

    @Transactional
    public CreateApplicationResponse apply(
            Integer userId,
            CreateApplicationRequest request
    ) {
        User user = getUser(userId);

        Gathering gathering = getGathering(request.gatheringId());

        validateDuplicateApplication(userId, gathering.getId());

        validateAlreadyMember(userId, gathering.getId());

        Application application = Application.builder()
                        .user(user)
                        .gathering(gathering)
                        .status(ApplicationStatus.PENDING)
                        .build();

        Application saved = applicationRepository.save(application);

        return CreateApplicationResponse.builder()
                .applicationId(saved.getId())
                .gatheringId(gathering.getId())
                .gatheringName(gathering.getGatheringName())
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }

    private Gathering getGathering(Integer gatheringId) {
        return gatheringRepository.findById(gatheringId)
                .orElseThrow(() -> new BusinessException(ErrorCode.GATHERING_NOT_FOUND));
    }

    private void validateDuplicateApplication(Integer userId, Integer gatheringId) {
        if (applicationRepository.existsByUserIdAndGatheringId(userId, gatheringId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_APPLICATION);
        }
    }

    private void validateAlreadyMember(Integer userId, Integer gatheringId) {
        GatheringMemberId memberId =
                new GatheringMemberId(gatheringId, userId);

        if (memberRepository.existsById(memberId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_GATHERING_MEMBER);
        }
    }

    public List<MyApplicationResponse> getMyApplications(Integer userId) {
        User user = getUser(userId);

        return applicationRepository.findMyApplications(user.getId())
                .stream()
                .map(application -> MyApplicationResponse.builder()
                        .applicationId(application.getId())
                        .gatheringId(application.getGathering().getId())
                        .gatheringName(application.getGathering().getGatheringName())
                        .status(application.getStatus())
                        .createdAt(application.getCreatedAt())
                        .updatedAt(application.getUpdatedAt())
                        .build())
                .toList();
    }
/*
    public List<GatheringApplicationResponse> getGatheringApplication(
            Integer userId,
            Integer gatheringId,
            ApplicationStatus status
    ) {
        User user = getUser(userId);
        Gathering gathering = getGathering(gatheringId);

        validateManager(user, gathering);

        return applicationRepository.findGatheringApplications(
                    gathering.getId(), status
                )
                .stream()
                .map(application -> GatheringApplicationResponse.builder()
                        .applicationId(application.getId())
                        .userId(application.getUser().getId())
                        .username(application.getUser().getUsername())
                        .status(application.getStatus())
                        .createdAt(application.getCreatedAt())
                        .updatedAt(application.getUpdatedAt())
                        .build())
                .toList();
    }

    private void validateManager(User user, Gathering gathering) {
        if (!gathering.getManager()
                .getId()
                .equals(user.getId())) {

            throw new BusinessException(ErrorCode.GATHERING_MANGER_ONLY);
        }
    }
    */
}
