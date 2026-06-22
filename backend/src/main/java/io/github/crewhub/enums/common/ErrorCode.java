package io.github.crewhub.enums.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 목록
 */
@Getter
public enum ErrorCode {
    INVALID_LOGIN(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DOCUMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "문서를 찾을 수 없습니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
    GATHERING_NOT_FOUND(HttpStatus.NOT_FOUND, "모임을 찾을 수 없습니다."),
    APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "지원서를 찾을 수 없습니다."),
    GATHERING_MEMBER_NOT_FOUND(HttpStatus.FORBIDDEN, "모임 회원을 찾을 수 없습니다."),

    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용중인 이메일입니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 사용중인 사용자명입니다."),
    DUPLICATE_GATHERING_NAME(HttpStatus.CONFLICT, "이미 사용중인 모임 이름입니다."),
    DUPLICATE_GATHERING_MEMBER(HttpStatus.CONFLICT, "이미 가입한 모임입니다."),
    DUPLICATE_APPLICATION(HttpStatus.CONFLICT, "이미 지원한 모임입니다."),
    LAST_MANAGER_CANNOT_BE_REMOVED(HttpStatus.CONFLICT, "모임에 관리자가 최소 1명 이상 존재해야 합니다."),
    ALREADY_MANAGER(HttpStatus.CONFLICT, "이미 관리자입니다."),

    APPLICATION_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, "이미 처리된 지원서입니다."),
    APPLICATION_NOT_CANCELLED(HttpStatus.BAD_REQUEST, "취소된 지원서만 복구할 수 있습니다."),
    CANNOT_KICK_SELF(HttpStatus.BAD_REQUEST, "자신은 강제 탈퇴시킬 수 없습니다."),
    CANNOT_TRANSFER_TO_SELF(HttpStatus.BAD_REQUEST, "자기 자신에게 관리자 권한을 위임할 수 없습니다."),

    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    GATHERING_MANAGER_ONLY(HttpStatus.FORBIDDEN, "모임 관리자만 가능합니다."),
    MANAGER_CANNOT_LEAVE(HttpStatus.FORBIDDEN, "모임 관리자는 탈퇴할 수 없습니다. 먼저 관리자 권한을 위임하세요."),

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
