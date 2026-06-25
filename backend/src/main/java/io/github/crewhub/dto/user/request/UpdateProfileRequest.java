package io.github.crewhub.dto.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * 사용자 프로필 수정 요청 데이터 
 */
@Schema(description = "프로필 수정 요청")
@Builder
public record UpdateProfileRequest(

        @Schema(
                description = "사용자명",
                example = "crewhub_user",
                minLength = 3,
                maxLength = 100,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank @Size(min = 3, max = 100)
        String username,

        @Schema(
                description = "자기소개",
                example = "백엔드 개발자를 준비하고 있습니다.",
                maxLength = 255
        )
        @Size(max = 255)
        String description,

        @Schema(
                description = "프로필 이미지 URL",
                example = "https://cdn.crewhub.com/profile/user1.png",
                maxLength = 255
        )
        @Size(max = 255)
        String profileImage
) {
}
