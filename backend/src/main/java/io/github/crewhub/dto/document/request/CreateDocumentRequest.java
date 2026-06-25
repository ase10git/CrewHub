package io.github.crewhub.dto.document.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

/**
 * 문서 작성 요청 데이터
 */
@Schema(description = "문서 작성 요청")
@Builder
public record CreateDocumentRequest(

        @Schema(
                description = "모임 ID",
                example = "1"
        )
        Integer gatheringId,

        @Schema(
                description = "문서 제목",
                example = "2026년 7월 스터디 계획",
                minLength = 5,
                maxLength = 255
        )
        @NotBlank @Size(min = 5, max = 255) String title,

        @Schema(
                description = "문서 내용",
                example = "# 목표\nSpring Security 학습\n\n# 일정\n매주 토요일 진행"
        )
        @NotBlank String content,

        @Schema(
                description = "문서 카테고리 ID 목록",
                example = "[1, 2]"
        )
        @NotEmpty List<Integer> categoryIds
) {
}
