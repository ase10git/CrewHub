package io.github.crewhub.controller.application;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.application.request.CreateApplicationRequest;
import io.github.crewhub.dto.application.response.CancelApplicationResponse;
import io.github.crewhub.dto.application.response.CreateApplicationResponse;
import io.github.crewhub.dto.application.response.MyApplicationResponse;
import io.github.crewhub.dto.application.response.ProcessApplicationResponse;
import io.github.crewhub.dto.common.PageResponse;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.service.application.ApplicationService;
import io.github.crewhub.swagger.annotation.application.*;
import io.github.crewhub.swagger.response.badrequest.ApplicationAlreadyProcessedResponse;
import io.github.crewhub.swagger.response.badrequest.ApplicationNotCancelledResponse;
import io.github.crewhub.swagger.response.conflict.DuplicateApplicationOrMemberResponse;
import io.github.crewhub.swagger.response.forbidden.GatheringManagerOnlyResponse;
import io.github.crewhub.swagger.response.notfound.ApplicationNotFoundResponse;
import io.github.crewhub.swagger.response.notfound.UserNotFoundResponse;
import io.github.crewhub.swagger.response.notfound.UserOrGatheringNotFoundResponse;
import io.github.crewhub.swagger.response.unauthorized.UnauthorizedResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 지원서 컨트롤러
 */
@Tag(
        name = "Application",
        description = "모임 가입 신청 및 신청서 관리 API"
)
@UnauthorizedResponse
@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @ApplicationApplyApi
    @UserOrGatheringNotFoundResponse
    @DuplicateApplicationOrMemberResponse
    @PostMapping
    public ApiResponse<CreateApplicationResponse> apply(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateApplicationRequest request
            ) {
        return ApiResponse.success(
                applicationService.apply(userDetails.getUserId(), request)
        );
    }

    @MyApplicationApi
    @UserNotFoundResponse
    @GetMapping("/my")
    public ApiResponse<PageResponse<MyApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ApiResponse.success(
                applicationService.getMyApplications(
                        userDetails.getUserId(),
                        page,
                        size
                )
        );
    }

    @ApplicationCancelApi
    @ApplicationNotFoundResponse
    @ApplicationAlreadyProcessedResponse
    @PatchMapping("/{applicationId}/cancel")
    public ApiResponse<CancelApplicationResponse> cancelApplication(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer applicationId
    ) {
        return ApiResponse.success(
                applicationService.cancelApplication(userDetails.getUserId(), applicationId)
        );
    }

    @ApplicationRevertApi
    @ApplicationNotFoundResponse
    @ApplicationNotCancelledResponse
    @PatchMapping("/{applicationId}/revert")
    public ApiResponse<CancelApplicationResponse> revertApplication(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer applicationId
    ) {
        return ApiResponse.success(
                applicationService.revertApplication(userDetails.getUserId(), applicationId)
        );
    }

    @ApplicationApproveApi
    @GatheringManagerOnlyResponse
    @ApplicationNotFoundResponse
    @DuplicateApplicationOrMemberResponse
    @PostMapping("/{applicationId}/approve")
    public ApiResponse<ProcessApplicationResponse> approve(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer applicationId
    ) {
        return ApiResponse.success(
                applicationService.approve(userDetails.getUserId(), applicationId)
        );
    }

    @ApplicationRejectApi
    @GatheringManagerOnlyResponse
    @ApplicationNotFoundResponse
    @ApplicationAlreadyProcessedResponse
    @PostMapping("/{applicationId}/reject")
    public ApiResponse<ProcessApplicationResponse> reject(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer applicationId
    ) {
        return ApiResponse.success(
                applicationService.reject(userDetails.getUserId(), applicationId)
        );
    }
}
