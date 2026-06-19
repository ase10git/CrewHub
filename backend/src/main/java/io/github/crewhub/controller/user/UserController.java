package io.github.crewhub.controller.user;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.user.request.UpdateProfileRequest;
import io.github.crewhub.dto.user.response.UserProfileResponse;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 정보 요청 처리
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        return ApiResponse.success(userService.getUserProfile(userDetails.getUserId()));
    }

    @GetMapping("/{userId}")
    public ApiResponse<UserProfileResponse> getUserProfile(
            @PathVariable Integer userId
    ) {
        return ApiResponse.success(userService.getUserProfile(userId));
    }

    @PutMapping("/me")
    public ApiResponse<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request
            ) {
        return ApiResponse.success(userService.updateProfile(userDetails.getUserId(), request));
    }
}
