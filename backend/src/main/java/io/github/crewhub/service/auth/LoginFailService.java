package io.github.crewhub.service.auth;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.config.auth.LoginFailProperties;
import io.github.crewhub.entity.auth.LoginFailUser;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.repository.auth.LoginFailUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로그인 실패 관리 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoginFailService {
    private final LoginFailUserRepository loginFailUserRepository;
    private final LoginFailProperties loginFailProperties;

    public LoginFailUser checkBlocked(String email) {
        return loginFailUserRepository.findById(email)
                .map(data -> {
                            if (data.isBlocked(loginFailProperties.maxCount())) {
                                throw new BusinessException(ErrorCode.TOO_MANY_LOGIN_ATTEMPTS);
                            }
                            return data;
                        }
                ).orElse(
                        LoginFailUser.builder()
                                .email(email)
                                .failCount(0)
                                .ttl(loginFailProperties.ttlSeconds())
                                .build()
                );
    }

    @Transactional
    public void fail(LoginFailUser loginFailUser) {
        loginFailUser.increase();
        loginFailUserRepository.save(loginFailUser);
    }

    @Transactional
    public void clear(String email) {
        loginFailUserRepository.deleteById(email);
    }
}
