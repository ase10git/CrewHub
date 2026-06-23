package io.github.crewhub.dto.document.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

/**
 * 문서 작성 요청 데이터
 */
@Builder
public record CreateDocumentRequest(

        Integer gatheringId,
        @NotBlank @Size(min = 5, max = 255) String title,
        @NotBlank String content,
        @NotEmpty List<Integer> categoryIds
) {
}
