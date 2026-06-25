package io.github.crewhub.controller.user;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.user.request.UpdateProfileRequest;
import io.github.crewhub.dto.user.response.UserProfileResponse;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.service.user.UserService;
import io.github.crewhub.swagger.annotation.user.MyProfileApi;
import io.github.crewhub.swagger.annotation.user.UpdateProfileApi;
import io.github.crewhub.swagger.annotation.user.UserProfileApi;
import io.github.crewhub.swagger.response.conflict.DuplicateUsernameResponse;
import io.github.crewhub.swagger.response.notfound.UserNotFoundResponse;
import io.github.crewhub.swagger.response.unauthorized.UnauthorizedResponse;
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
@UnauthorizedResponse
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @MyProfileApi
    @UserNotFoundResponse
    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getMyProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        return ApiResponse.success(userService.getUserProfile(userDetails.getUserId()));
    }

    @UserProfileApi
    @UserNotFoundResponse
    @GetMapping("/{userId}")
    public ApiResponse<UserProfileResponse> getUserProfile(
            @PathVariable Integer userId
    ) {
        return ApiResponse.success(userService.getUserProfile(userId));
    }

    @UpdateProfileApi
    @UserNotFoundResponse
    @DuplicateUsernameResponse
    @PutMapping("/me")
    public ApiResponse<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody UpdateProfileRequest request
            ) {
        return ApiResponse.success(userService.updateProfile(userDetails.getUserId(), request));
    }
}
