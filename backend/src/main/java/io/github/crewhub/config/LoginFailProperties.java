package io.github.crewhub.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 로그인 실패 관리용 설정값을 저장한 데이터
 */
@ConfigurationProperties(prefix = "security.login-fail")
public record LoginFailProperties(
        int maxCount,
        int ttlSeconds
) {
}
