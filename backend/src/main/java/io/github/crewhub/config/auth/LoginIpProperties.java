package io.github.crewhub.config.auth;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 로그인 시도 제한 관리용 설정값을 저장한 데이터
 */
@ConfigurationProperties(prefix = "security.login-ip")
public record LoginIpProperties(
        int maxCount,
        int ttlSeconds
) {
}
