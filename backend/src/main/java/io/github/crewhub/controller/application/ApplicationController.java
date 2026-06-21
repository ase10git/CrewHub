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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 지원서 컨트롤러
 */
@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class ApplicationController {
    private final ApplicationService applicationService;

    @PostMapping
    public ApiResponse<CreateApplicationResponse> apply(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateApplicationRequest request
            ) {
        return ApiResponse.success(
                applicationService.apply(userDetails.getUserId(), request)
        );
    }

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

    @PatchMapping("/{applicationId}/cancel")
    public ApiResponse<CancelApplicationResponse> cancelApplication(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer applicationId
    ) {
        return ApiResponse.success(
                applicationService.cancelApplication(userDetails.getUserId(), applicationId)
        );
    }

    @PostMapping("/{applicationId}/approve")
    public ApiResponse<ProcessApplicationResponse> approve(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer applicationId
    ) {
        return ApiResponse.success(
                applicationService.approve(userDetails.getUserId(), applicationId)
        );
    }
}
