package io.github.crewhub.controller.document;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.common.PageResponse;
import io.github.crewhub.dto.document.request.CreateDocumentRequest;
import io.github.crewhub.dto.document.response.CreateDocumentResponse;
import io.github.crewhub.dto.document.response.DocumentDetailResponse;
import io.github.crewhub.dto.document.response.DocumentSummaryResponse;
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
}
