package io.github.crewhub.dto.document.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 문서 상세 정보 응답 데이터
 */
@Schema(description = "문서 상세 정보 응답")
@Builder
public record DocumentDetailResponse(
        @Schema(
                description = "문서 ID",
                example = "100"
        )
        Integer documentId,


        @Schema(
                description = "문서 카테고리 목록"
        )
        List<CategoryResponse> categoryList,

        @Schema(
                description = "작성자 ID",
                example = "5"
        )
        Integer writerId,

        @Schema(
                description = "작성자 이름",
                example = "홍길동"
        )
        String writerName,

        @Schema(
                description = "모임 ID",
                example = "10"
        )
        Integer gatheringId,

        @Schema(
                description = "모임 이름",
                example = "Spring 개발자 모임"
        )
        String gatheringName,

        @Schema(
                description = "문서 제목",
                example = "JPA 영속성 컨텍스트 정리"
        )
        String title,

        @Schema(
                description = "문서 내용",
                example = "영속성 컨텍스트는 엔티티를 관리하는 환경입니다."
        )
        String content,

        @Schema(
                description = "조회수",
                example = "152"
        )
        Integer views,

        @Schema(
                description = "삭제 여부",
                example = "false"
        )
        boolean isDeleted,

        @Schema(
                description = "작성 시간",
                example = "2026-06-25T10:30:00"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "수정 시간",
                example = "2026-06-25T12:00:00"
        )
        LocalDateTime updatedAt
) {
}
