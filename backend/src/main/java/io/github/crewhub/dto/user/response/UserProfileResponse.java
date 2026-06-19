package io.github.crewhub.dto.user.response;

import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.user.UserStatus;
import lombok.Builder;

/**
 * 사용자 프로필 응답 데이터
 */
@Builder
public record UserProfileResponse(
        Integer userId,
        String username,
        String description,
        String profileImage,
        UserStatus status
) {
    public static UserProfileResponse from(User user) {

        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getDescription(),
                user.getProfileImage(),
                user.getStatus()
        );
    }
}
