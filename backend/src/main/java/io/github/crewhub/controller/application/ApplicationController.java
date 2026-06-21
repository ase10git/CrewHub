package io.github.crewhub.controller.application;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.application.request.CreateApplicationRequest;
import io.github.crewhub.dto.application.response.CreateApplicationResponse;
import io.github.crewhub.dto.application.response.MyApplicationResponse;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.service.application.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ApiResponse<List<MyApplicationResponse>> getMyApplications(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.success(
                applicationService.getMyApplications(userDetails.getUserId())
        );
    }

}
