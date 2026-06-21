package io.github.crewhub.enums.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 목록
 */
@Getter
public enum ErrorCode {
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "문서를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    GATHERING_NOT_FOUND(HttpStatus.NOT_FOUND, "모임을 찾을 수 없습니다."),

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 사용중인 사용자명입니다."),
    DUPLICATE_GATHERING_NAME(HttpStatus.CONFLICT, "이미 사용중인 모임 이름입니다."),
    DUPLICATE_GATHERING_MEMBER(HttpStatus.CONFLICT, "이미 가입한 모임입니다."),
    DUPLICATE_APPLICATION(HttpStatus.CONFLICT, "이미 지원한 모임입니다."),

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    GATHERING_ACCESS_DENIED(HttpStatus.FORBIDDEN, "모임 관리자만 가능합니다."),

    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "서버 내부 오류가 발생했습니다."
    );

    private final HttpStatus status;
    private final String message;

    ErrorCode(
            HttpStatus status,
            String message
    ) {
        this.status = status;
        this.message = message;
    }
}
