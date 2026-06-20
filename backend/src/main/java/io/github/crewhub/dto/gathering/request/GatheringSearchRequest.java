package io.github.crewhub.dto.gathering.request;

import lombok.Builder;

/**
 * 모임 검색 요청 데이터
 */
@Builder
public record GatheringSearchRequest(
        String keyword,
        Integer categoryId
) {
}
