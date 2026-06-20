package io.github.crewhub.controller.gathering;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.gathering.request.CreateGatheringRequest;
import io.github.crewhub.dto.gathering.response.CreateGatheringResponse;
import io.github.crewhub.dto.gathering.response.GatheringDetailResponse;
import io.github.crewhub.dto.gathering.response.GatheringSummaryResponse;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.service.gathering.GatheringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 모임 정보 요청 처리
 */
@RestController
@RequestMapping("/api/gathering")
@RequiredArgsConstructor
public class GatheringController {
    private final GatheringService gatheringService;

    @GetMapping
    public ApiResponse<List<GatheringSummaryResponse>> getGatherings() {
        return ApiResponse.success(
                gatheringService.getGatherings()
        );
    }

    @GetMapping("/{gatheringId}")
    public ApiResponse<GatheringDetailResponse> getGathering(
            @PathVariable Integer gatheringId
    ) {
        return ApiResponse.success(
                gatheringService.getGathering(gatheringId)
        );
    }

    @GetMapping("/search")
    public ApiResponse<List<GatheringSummaryResponse>> searchByName(
            @RequestParam String keyword
    ) {
        return ApiResponse.success(
                gatheringService.searchByName(keyword)
        );
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<GatheringSummaryResponse>> searchByCategory(
            @PathVariable Integer categoryId
    ) {
        return ApiResponse.success(
                gatheringService.searchByCategory(categoryId)
        );
    }

    @PostMapping
    public ApiResponse<CreateGatheringResponse> create(
            @Valid @RequestBody CreateGatheringRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        return ApiResponse.success(gatheringService.create(userDetails.getUserId(), request));
    }
}
