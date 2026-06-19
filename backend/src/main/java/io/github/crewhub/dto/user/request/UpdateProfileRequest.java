package io.github.crewhub.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

/**
 * 사용자 프로필 수정 요청 데이터 
 */
@Builder
public record UpdateProfileRequest(
        @NotBlank @Size(min = 3, max = 100)
        String username,
        @Size(max = 255)
        String description,
        @Size(max = 255)
        String profileImage
) {
}
