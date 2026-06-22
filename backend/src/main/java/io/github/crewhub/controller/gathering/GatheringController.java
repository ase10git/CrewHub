package io.github.crewhub.controller.gathering;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.application.response.GatheringApplicationResponse;
import io.github.crewhub.dto.common.PageResponse;
import io.github.crewhub.dto.gathering.request.CreateGatheringRequest;
import io.github.crewhub.dto.gathering.request.UpdateGatheringRequest;
import io.github.crewhub.dto.gathering.response.*;
import io.github.crewhub.enums.application.ApplicationStatus;
import io.github.crewhub.enums.gathering.MemberRole;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.service.application.ApplicationService;
import io.github.crewhub.service.gathering.GatheringMemberService;
import io.github.crewhub.service.gathering.GatheringService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 모임 정보 요청 처리
 */
@RestController
@RequestMapping("/api/gathering")
@RequiredArgsConstructor
public class GatheringController {
    private final GatheringService gatheringService;
    private final GatheringMemberService gatheringMemberService;
    private final ApplicationService applicationService;

    @GetMapping
    public ApiResponse<PageResponse<GatheringSummaryResponse>> getGatherings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
                gatheringService.getGatherings(page, size)
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
    public ApiResponse<PageResponse<GatheringSummaryResponse>> searchByName(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
                gatheringService.searchByName(keyword, page, size)
        );
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<PageResponse<GatheringSummaryResponse>> searchByCategory(
            @PathVariable Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
                gatheringService.searchByCategory(categoryId, page, size)
        );
    }

    @PostMapping
    public ApiResponse<CreateGatheringResponse> create(
            @Valid @RequestBody CreateGatheringRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        return ApiResponse.success(gatheringService.create(userDetails.getUserId(), request));
    }

    @PutMapping("/{gatheringId}")
    public ApiResponse<UpdateGatheringResponse> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer gatheringId,
            @Valid @RequestBody UpdateGatheringRequest request
    ) {
        return ApiResponse.success(
                gatheringService.update(
                        userDetails.getUserId(),
                        gatheringId,
                        request
                )
        );
    }

    @DeleteMapping("/{gatheringId}")
    public ApiResponse<Void> delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer gatheringId
    ) {
        gatheringService.delete(
                userDetails.getUserId(),
                gatheringId
        );

        return ApiResponse.success(
                "모임이 삭제되었습니다.", null
        );
    }

    @GetMapping("/application/{gatheringId}")
    public ApiResponse<PageResponse<GatheringApplicationResponse>> getGatheringApplications(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer gatheringId,
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        return ApiResponse.success(
                applicationService.getGatheringApplications(
                        userDetails.getUserId(),
                        gatheringId,
                        status,
                        page,
                        size
                )
        );
    }

    /**
     * 모임 회원 관리
     */

    @GetMapping("/{gatheringId}/members")
    public ApiResponse<PageResponse<GatheringMemberResponse>> getMembers(
            @PathVariable Integer gatheringId,
            @RequestParam(required = false) MemberRole role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
                gatheringMemberService.getMembers(gatheringId, role, page, size)
        );
    }

    @DeleteMapping("/{gatheringId}/members/leave")
    public ApiResponse<LeaveGatheringResponse> leave(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer gatheringId
    ) {
        return ApiResponse.success(
                gatheringMemberService.leave(userDetails.getUserId(), gatheringId)
        );
    }

    @DeleteMapping("/{gatheringId}/members/{userId}")
    public ApiResponse<KickMemberResponse> kickMember(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer gatheringId,
            @PathVariable Integer userId
    ) {
        return ApiResponse.success(
                gatheringMemberService.kickMember(userDetails.getUserId(), gatheringId, userId)
        );
    }

    @PatchMapping("/{gatheringId}/manager/{userId}")
    public ApiResponse<TransferManagerResponse> transferManager(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer gatheringId,
            @PathVariable Integer userId
    ) {
        return ApiResponse.success(
                gatheringMemberService.transferManager(userDetails.getUserId(), gatheringId, userId)
        );
    }

    @GetMapping("/my")
    public ApiResponse<PageResponse<MyGatheringResponse>> getMyGatherings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
                gatheringMemberService.getMyGatherings(userDetails.getUserId(), page, size)
        );
    }

    @GetMapping("/{gatheringId}/members/count")
    public ApiResponse<GatheringMemberCountResponse> getMemberCount(
            @PathVariable Integer gatheringId
    ) {
        return ApiResponse.success(gatheringMemberService.getMemberCount(gatheringId));
    }

    @GetMapping("/{gatheringId}/members/me")
    public ApiResponse<CheckMembershipResponse> checkMembership(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer gatheringId
    ) {
        return ApiResponse.success(
                gatheringMemberService.checkMembership(userDetails.getUserId(), gatheringId)
        );
    }

    @GetMapping("/{gatheringId}/members/me/manager")
    public ApiResponse<CheckManagerResponse> checkManager(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer gatheringId
    ) {
        return ApiResponse.success(
                gatheringMemberService.checkManager(userDetails.getUserId(), gatheringId)
        );
    }
}
