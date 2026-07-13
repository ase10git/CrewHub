package io.github.crewhub.security.csrf;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.token.CsrfTokenInfo;
import io.github.crewhub.dto.token.RefreshTokenInfo;
import io.github.crewhub.enums.common.ErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * CSRF Token 발급
 */
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CsrfTokenProvider {
    @Value("${csrf.secret-key}")
    private String secretKey;

    private static final SecureRandom secureRandom = new SecureRandom();

    private static final int TOKEN_BYTE_LENGTH = 32;

    public CsrfTokenInfo generate(RefreshTokenInfo refreshTokenInfo) {
        byte[] randomBytes = new byte[TOKEN_BYTE_LENGTH];
        secureRandom.nextBytes(randomBytes);
        String randomString = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        String message = refreshTokenInfo.jti() + ":" + randomString;

        String signature = generateHMAC(message);

        String token = signature + "." + randomString;

        return CsrfTokenInfo.builder()
                .csrfToken(token)
                .ttlSeconds(refreshTokenInfo.ttlSeconds())
                .build();
    }

    public String generateHMAC(String message){
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(
                    secretKey.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(message.getBytes());
            return Base64.getUrlEncoder().withoutPadding().encodeToString(hmacBytes);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void validateCsrfToken(String cookieToken, String headerToken, String jti) {
        String[] cookieTokenParts = cookieToken.split("\\.");
        String[] headerTokenParts = headerToken.split("\\.");

        if (cookieTokenParts.length != 2 || headerTokenParts.length != 2) {
            throw new BusinessException(ErrorCode.INVALID_CSRF_TOKEN);
        }

        String cookieTokenSignature = cookieTokenParts[0];
        String cookieRandomString = cookieTokenParts[1];

        String headerTokenSignature = headerTokenParts[0];
        String headerRandomString = headerTokenParts[1];

        if (!(safeEquals(headerRandomString, cookieRandomString) &&
                safeEquals(headerTokenSignature, cookieTokenSignature))) {
            throw new BusinessException(ErrorCode.INVALID_CSRF_TOKEN);
        }

        String cookieMessage = jti + ":" + cookieRandomString;
        String expectedSignature = generateHMAC(cookieMessage);

        if (!safeEquals(expectedSignature, cookieTokenSignature)) {
            throw new BusinessException(ErrorCode.INVALID_CSRF_TOKEN);
        }
    }

    private boolean safeEquals(String a, String b) {
        return MessageDigest.isEqual(
                a.getBytes(StandardCharsets.UTF_8),
                b.getBytes(StandardCharsets.UTF_8)
        );
    }
}
