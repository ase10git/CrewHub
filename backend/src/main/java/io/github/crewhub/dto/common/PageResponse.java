package io.github.crewhub.dto.common;

import lombok.Builder;

import java.util.List;

/**
 * 페이지 응답 
 */
@Builder
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext
) {
}