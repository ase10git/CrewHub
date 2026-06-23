package io.github.crewhub.dto.document.response;

import lombok.Builder;

/**
 * 내 문서 수 응답 데이터
 */
@Builder
public record MyDocumentCountResponse(
        Integer userId,
        Long documentCount
) {
}
