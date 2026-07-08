package io.github.crewhub.config.auth;

/**
 * 로그인 및 회원가입 입력값의 정규식
 */
public final class AuthValidationRegex {
    private AuthValidationRegex() {}

    // 한글, 영어, 숫자
    public static final String USERNAME = "^[가-힣A-Za-z][가-힣A-Za-z0-9_]{2,19}$";
    
    // 영어 대문자, 소문자, 숫자, 특수문자 최소 1개 이상 포함
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]+$";
}
