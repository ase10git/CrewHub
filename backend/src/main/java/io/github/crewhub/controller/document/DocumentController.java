package io.github.crewhub.controller.document;

import io.github.crewhub.common.response.ApiResponse;
import io.github.crewhub.dto.document.request.CreateDocumentRequest;
import io.github.crewhub.dto.document.response.CreateDocumentResponse;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.service.document.DocumentService;
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
@RequestMapping("/api/document")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping
    public ApiResponse<CreateDocumentResponse> create(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody CreateDocumentRequest request
    ) {

        return ApiResponse.success(
                documentService.create(userDetails.getUserId(), request)
        );
    }
}
