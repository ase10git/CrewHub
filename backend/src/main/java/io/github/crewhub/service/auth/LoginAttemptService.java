package io.github.crewhub.service.auth;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.config.auth.LoginIpProperties;
import io.github.crewhub.entity.auth.LoginAttemptIp;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.repository.auth.LoginAttemptIpRepository;
import io.github.crewhub.security.client.ClientResolver;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로그인 시도 관리 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginAttemptService {
    private final LoginAttemptIpRepository loginAttemptIpRepository;
    private final LoginIpProperties loginIpProperties;

    private final ClientResolver clientResolver;

    public void recordLoginAttempt(HttpServletRequest request) {
        String clientIp = clientResolver.resolveIp(request);

        LoginAttemptIp loginAttemptIp = loginAttemptIpRepository
                .findById(clientIp)
                .orElse(
                        LoginAttemptIp.builder()
                                .ip(clientIp)
                                .attemptCount(0)
                                .ttl(loginIpProperties.ttlSeconds())
                                .build()
                );

        if (loginAttemptIp.isBlocked(loginIpProperties.maxCount())) {
            throw new BusinessException(ErrorCode.TOO_MANY_LOGIN_ATTEMPTS);
        }

        increase(loginAttemptIp);
    }

    @Transactional
    public void increase(LoginAttemptIp loginAttemptIp) {
        loginAttemptIp.increase();
        loginAttemptIpRepository.save(loginAttemptIp);
    }
}
