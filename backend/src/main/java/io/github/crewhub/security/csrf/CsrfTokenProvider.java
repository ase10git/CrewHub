package io.github.crewhub.security.csrf;

import io.github.crewhub.dto.token.CsrfTokenInfo;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * CSRF Token 발급
 */
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CsrfTokenProvider {
    private static final SecureRandom secureRandom = new SecureRandom();

    private static final int TOKEN_BYTE_LENGTH = 32;

    public CsrfTokenInfo generate(Long ttlSeconds) {
        byte[] randomBytes = new byte[TOKEN_BYTE_LENGTH];
        secureRandom.nextBytes(randomBytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        return CsrfTokenInfo.builder()
                .csrfToken(token)
                .ttlSeconds(ttlSeconds)
                .build();
    }
}
