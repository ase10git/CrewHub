package io.github.crewhub.dto.document.response;

import lombok.Builder;

/**
 * 카테고리 정보 응답 데이터
 */
@Builder
public record CategoryResponse(
        Integer categoryId,
        String key,
        String label
) {
}
