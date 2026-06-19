package io.github.crewhub.common.response;

import lombok.Builder;
import lombok.Getter;

/**
 * API 공통 에러 응답 클래스
 */
@Getter
@Builder
public class ErrorResponse {

    private final boolean success;
    private final String code;
    private final String message;
}