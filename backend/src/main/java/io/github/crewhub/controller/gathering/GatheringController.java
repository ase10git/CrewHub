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
import io.github.crewhub.swagger.annotation.document.GetGatheringCategoriesApi;
import io.github.crewhub.swagger.annotation.gathering.*;
import io.github.crewhub.swagger.response.badrequest.CannotKickSelfResponse;
import io.github.crewhub.swagger.response.badrequest.CannotTransferToSelfResponse;
import io.github.crewhub.swagger.response.badrequest.InvalidKeywordResponse;
import io.github.crewhub.swagger.response.conflict.AlreadyManagerResponse;
import io.github.crewhub.swagger.response.conflict.DuplicateGatheringNameResponse;
import io.github.crewhub.swagger.response.conflict.LastManagerCannotBeRemovedResponse;
import io.github.crewhub.swagger.response.forbidden.GatheringManagerOnlyResponse;
import io.github.crewhub.swagger.response.forbidden.GatheringMemberOnlyResponse;
import io.github.crewhub.swagger.response.forbidden.MemberOnlyOrManagerCannotLeaveResponse;
import io.github.crewhub.swagger.response.notfound.*;
import io.github.crewhub.swagger.response.unauthorized.UnauthorizedResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 모임 정보 요청 처리
 */
@Tag(
        name = "Gathering",
        description = "모임 및 회원 정보 API"
)
@RestController
@RequestMapping("/api/gathering")
@RequiredArgsConstructor
public class GatheringController {
    private final GatheringService gatheringService;
    private final GatheringMemberService gatheringMemberService;
    private final ApplicationService applicationService;

    @GetGatheringsApi
    @GetMapping
    public ApiResponse<PageResponse<GatheringSummaryResponse>> getGatherings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
                gatheringService.getGatherings(page, size)
        );
    }

    @GetGatheringApi
    @GatheringNotFoundResponse
    @GetMapping("/{gatheringId}")
    public ApiResponse<GatheringDetailResponse> getGathering(
            @PathVariable Integer gatheringId
    ) {
        return ApiResponse.success(
                gatheringService.getGathering(gatheringId)
        );
    }

    @SearchGatheringByNameApi
    @InvalidKeywordResponse
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

    @SearchGatheringByCategoryApi
    @CategoryNotFoundResponse
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

    @GetGatheringCategoriesApi
    @GetMapping("/category")
    public ApiResponse<List<GatheringCategoryResponse>> getCategories() {
        return ApiResponse.success(
                gatheringService.getCategories()
        );
    }

    @CreateGatheringApi
    @UnauthorizedResponse
    @DuplicateGatheringNameResponse
    @UserOrCategoryNotFoundResponse
    @PostMapping
    public ApiResponse<CreateGatheringResponse> create(
            @Valid @RequestBody CreateGatheringRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
            ) {
        return ApiResponse.success(gatheringService.create(userDetails.getUserId(), request));
    }

    @UpdateGatheringApi
    @UnauthorizedResponse
    @GatheringNotFoundResponse
    @GatheringManagerOnlyResponse
    @DuplicateGatheringNameResponse
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

    @DeleteGatheringApi
    @UnauthorizedResponse
    @GatheringNotFoundResponse
    @GatheringManagerOnlyResponse
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

    @GetGatheringApplicationsApi
    @UnauthorizedResponse
    @GatheringManagerOnlyResponse
    @GatheringNotFoundResponse
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

    @GetMembersApi
    @UnauthorizedResponse
    @GatheringNotFoundResponse
    @GatheringMemberOnlyResponse
    @GetMapping("/{gatheringId}/members")
    public ApiResponse<PageResponse<GatheringMemberResponse>> getMembers(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer gatheringId,
            @RequestParam(required = false) MemberRole role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
                gatheringMemberService.getMembers(
                        userDetails.getUserId(),
                        gatheringId,
                        role,
                        page,
                        size
                )
        );
    }

    @LeaveGatheringApi
    @UnauthorizedResponse
    @MemberOnlyOrManagerCannotLeaveResponse
    @DeleteMapping("/{gatheringId}/members/leave")
    public ApiResponse<LeaveGatheringResponse> leave(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer gatheringId
    ) {
        return ApiResponse.success(
                gatheringMemberService.leave(userDetails.getUserId(), gatheringId)
        );
    }

    @KickMemberApi
    @UnauthorizedResponse
    @GatheringOrMemberNotFound
    @GatheringManagerOnlyResponse
    @LastManagerCannotBeRemovedResponse
    @CannotKickSelfResponse
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

    @TransferManagerApi
    @UnauthorizedResponse
    @GatheringManagerOnlyResponse
    @CannotTransferToSelfResponse
    @GatheringOrMemberNotFound
    @AlreadyManagerResponse
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

    @MyGatheringsApi
    @UnauthorizedResponse
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

    @GetMemberCountApi
    @GatheringNotFoundResponse
    @GetMapping("/{gatheringId}/members/count")
    public ApiResponse<GatheringMemberCountResponse> getMemberCount(
            @PathVariable Integer gatheringId
    ) {
        return ApiResponse.success(gatheringMemberService.getMemberCount(gatheringId));
    }

    @CheckMembershipApi
    @UnauthorizedResponse
    @GetMapping("/{gatheringId}/members/me")
    public ApiResponse<CheckMembershipResponse> checkMembership(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer gatheringId
    ) {
        return ApiResponse.success(
                gatheringMemberService.checkMembership(userDetails.getUserId(), gatheringId)
        );
    }

    @CheckManagerApi
    @UnauthorizedResponse
    @GatheringMemberOnlyResponse
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
