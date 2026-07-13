package io.github.crewhub.service.user;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.user.request.UpdateProfileRequest;
import io.github.crewhub.dto.user.response.UserProfileResponse;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 정보 처리 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public User getUser(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
                );
    }

    public UserProfileResponse getUserProfile(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
                );
        return UserProfileResponse.from(user);
    }

    @Transactional
    public UserProfileResponse updateProfile(Integer userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
                );

        validateDuplicatedUsername(request.username(), user.getId());

        user.updateProfile(request.username(), request.description(), request.profileImage());

        return UserProfileResponse.from(user);
    }

    private void validateDuplicatedUsername(String username, Integer userId) {
        if (userRepository.existsByUsernameAndIdNot(username, userId)) {
            throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
        }
    }
}
