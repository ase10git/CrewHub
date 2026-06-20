package io.github.crewhub.controller.gathering;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.gathering.request.CreateGatheringRequest;
import io.github.crewhub.dto.gathering.response.CreateGatheringResponse;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.service.gathering.GatheringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 모임 정보 요청 처리
 */
@RestController
@RequestMapping("/api/gathering")
@RequiredArgsConstructor
public class GatheringController {
    private final GatheringService gatheringService;

    @PostMapping
    public ApiResponse<CreateGatheringResponse> create(
            @Valid @RequestBody CreateGatheringRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        return ApiResponse.success(gatheringService.create(userDetails.getUserId(), request));
    }
}
