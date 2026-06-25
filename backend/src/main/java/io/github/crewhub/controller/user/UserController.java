package io.github.crewhub.controller.user;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.user.request.UpdateProfileRequest;
import io.github.crewhub.dto.user.response.UserProfileResponse;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.service.user.UserService;
import io.github.crewhub.swagger.annotation.user.MyProfileApi;
import io.github.crewhub.swagger.annotation.user.UpdateProfileApi;
import io.github.crewhub.swagger.annotation.user.UserProfileApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 정보 요청 처리
 */
@Tag(
        name = "User",
        description = "사용자 정보 API"
)
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @MyProfileApi
    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        return ApiResponse.success(userService.getUserProfile(userDetails.getUserId()));
    }

    @UserProfileApi
    @GetMapping("/{userId}")
    public ApiResponse<UserProfileResponse> getUserProfile(
            @PathVariable Integer userId
    ) {
        return ApiResponse.success(userService.getUserProfile(userId));
    }

    @UpdateProfileApi
    @PutMapping("/me")
    public ApiResponse<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request
            ) {
        return ApiResponse.success(userService.updateProfile(userDetails.getUserId(), request));
    }
}
