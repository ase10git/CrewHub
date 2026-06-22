package io.github.crewhub.controller.document;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.common.PageResponse;
import io.github.crewhub.dto.document.request.CreateDocumentRequest;
import io.github.crewhub.dto.document.request.UpdateDocumentRequest;
import io.github.crewhub.dto.document.response.*;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.service.document.DocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 모임 정보 요청 처리
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping("/gathering/document")
    public ApiResponse<CreateDocumentResponse> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateDocumentRequest request
    ) {

        return ApiResponse.success(
                documentService.create(userDetails.getUserId(), request)
        );
    }

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

    @GetMapping("/document/{documentId}")
    public ApiResponse<DocumentDetailResponse> getDocument(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Integer documentId
    ) {
        return ApiResponse.success(
                documentService.getDocument(userDetails.getUserId(), documentId)
        );
    }

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

    @GetMapping("/gathering/{gatheringId}/document/count")
    public ApiResponse<GatheringDocumentCountResponse> getDocumentCount(
            @PathVariable Integer gatheringId
    ) {
        return ApiResponse.success(documentService.getDocumentCount(gatheringId));
    }
}
