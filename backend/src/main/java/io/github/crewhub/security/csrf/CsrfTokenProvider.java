package io.github.crewhub.security.csrf;

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

    public static String generate() {
        byte[] randomBytes = new byte[TOKEN_BYTE_LENGTH];
        secureRandom.nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }
}
