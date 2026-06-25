package io.github.crewhub.dto.user.response;

import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.user.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

/**
 * 사용자 프로필 응답 데이터
 */
@Schema(description = "사용자 프로필 응답")
@Builder
public record UserProfileResponse(
        @Schema(
                description = "사용자 ID",
                example = "1"
        )
        Integer userId,

        @Schema(
                description = "사용자명",
                example = "crewhub_user"
        )
        String username,

        @Schema(
                description = "자기소개",
                example = "백엔드 개발자를 준비하고 있습니다."
        )
        String description,

        @Schema(
                description = "프로필 이미지 URL",
                example = "https://cdn.crewhub.com/profile/user1.png"
        )
        String profileImage,

        @Schema(
                description = "사용자 상태",
                example = "ACTIVE"
        )
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
