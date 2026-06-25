package io.github.crewhub.controller.document;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.common.PageResponse;
import io.github.crewhub.dto.document.request.CreateDocumentRequest;
import io.github.crewhub.dto.document.request.UpdateDocumentRequest;
import io.github.crewhub.dto.document.response.*;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.service.document.DocumentService;
import io.github.crewhub.swagger.annotation.document.*;
import io.github.crewhub.swagger.response.badrequest.InvalidKeywordResponse;
import io.github.crewhub.swagger.response.forbidden.AccessDeniedResponse;
import io.github.crewhub.swagger.response.forbidden.GatheringMemberOnlyResponse;
import io.github.crewhub.swagger.response.forbidden.MemberOrWriterOnlyResponse;
import io.github.crewhub.swagger.response.notfound.*;
import io.github.crewhub.swagger.response.unauthorized.UnauthorizedResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 문서 정보 요청 처리
 */
@Tag(
        name = "Document",
        description = "문서 API"
)
@UnauthorizedResponse
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @CreateDocumentApi
    @UserOrCategoryNotFoundResponse
    @GatheringMemberOnlyResponse
    @PostMapping("/document")
    public ApiResponse<CreateDocumentResponse> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateDocumentRequest request
    ) {

        return ApiResponse.success(
                documentService.create(userDetails.getUserId(), request)
        );
    }

    @GetDocumentsApi
    @GatheringMemberOnlyResponse
    @GetMapping("/gathering/{gatheringId}/document")
    public ApiResponse<PageResponse<DocumentSummaryResponse>> getDocuments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer gatheringId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
                documentService.getDocuments(
                        userDetails.getUserId(),
                        gatheringId,
                        page,
                        size
                )
        );
    }

    @GetDocumentApi
    @DocumentNotFoundResponse
    @GatheringMemberOnlyResponse
    @GetMapping("/document/{documentId}")
    public ApiResponse<DocumentDetailResponse> getDocument(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer documentId
    ) {
        return ApiResponse.success(
                documentService.getDocument(userDetails.getUserId(), documentId)
        );
    }

    @SearchDocumentByTitleApi
    @InvalidKeywordResponse
    @GatheringNotFoundResponse
    @GatheringMemberOnlyResponse
    @GetMapping("/gathering/{gatheringId}/document/search")
    public ApiResponse<PageResponse<DocumentSummaryResponse>> searchByName(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer gatheringId,
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
                documentService.searchByTitle(
                        userDetails.getUserId(),
                        gatheringId,
                        keyword,
                        page,
                        size
                )
        );
    }

    @SearchDocumentByCategoryApi
    @GatheringOrCategoryNotFoundResponse
    @GatheringMemberOnlyResponse
    @GetMapping("/gathering/{gatheringId}/document/category/{categoryId}")
    public ApiResponse<PageResponse<DocumentSummaryResponse>> searchByCategory(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer gatheringId,
            @PathVariable Integer categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
                documentService.searchByCategory(
                        userDetails.getUserId(),
                        gatheringId,
                        categoryId,
                        page,
                        size
                )
        );
    }

    @MyDocumentsApi
    @GetMapping("/document/my")
    public ApiResponse<PageResponse<MyDocumentResponse>> getMyDocuments(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.success(
                documentService.getMyDocuments(userDetails.getUserId(), page, size)
        );
    }

    @UpdateDocumentApi
    @DocumentPropertiesNotFoundResponse
    @MemberOrWriterOnlyResponse
    @PutMapping("/document/{documentId}")
    public ApiResponse<UpdateDocumentResponse> update(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer documentId,
            @Valid @RequestBody UpdateDocumentRequest request
    ) {
        return ApiResponse.success(
                documentService.update(
                        userDetails.getUserId(),
                        documentId,
                        request
                )
        );
    }

    @DeleteDocumentApi
    @AccessDeniedResponse
    @DeleteMapping("/document/{documentId}")
    public ApiResponse<Void> delete(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer documentId
    ) {
        documentService.delete(userDetails.getUserId(), documentId);

        return ApiResponse.success(
                "문서가 삭제되었습니다.", null
        );
    }

    @GetDocumentCountApi
    @GatheringNotFoundResponse
    @GetMapping("/gathering/{gatheringId}/document/count")
    public ApiResponse<GatheringDocumentCountResponse> getDocumentCount(
            @PathVariable Integer gatheringId
    ) {
        return ApiResponse.success(documentService.getDocumentCount(gatheringId));
    }

    @MyDocumentCountApi
    @GetMapping("/document/my/count")
    public ApiResponse<MyDocumentCountResponse> getMyDocumentCount(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ApiResponse.success(documentService.getMyDocumentCount(userDetails.getUserId()));
    }
}
