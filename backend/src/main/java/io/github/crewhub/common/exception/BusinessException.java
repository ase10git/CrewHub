package io.github.crewhub.common.exception;


import io.github.crewhub.enums.common.ErrorCode;
import lombok.Getter;

/**
 * 서비스 커스텀 예외
 */
@Getter
public class BusinessException extends RuntimeException{
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
