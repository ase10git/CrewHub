package io.github.crewhub.security.cookie;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Cookie 생성
 */
@Component
public class CookieProvider {

    @Value("${cookie.secure}")
    private boolean secure;

    @Value("${cookie.http-only}")
    private boolean httpOnly;

    @Value("${cookie.domain}")
    private String domain;

    @Value("${cookie.same-site}")
    private String sameSite;

    public ResponseCookie createRefreshTokenCookie(String refreshToken, long maxAgeMillis) {
        ResponseCookie.ResponseCookieBuilder builder =
                ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(httpOnly)
                .secure(secure)
                .domain("")
                .path("/")
                .maxAge(Duration.ofMillis(maxAgeMillis))
                .sameSite(sameSite);

        if (domain != null && !domain.isBlank()) {
            builder.domain(domain);
        }

        return builder.build();
    }

    public ResponseCookie deleteRefreshTokenCookie() {
        ResponseCookie.ResponseCookieBuilder builder =
                ResponseCookie.from("refreshToken", "")
                .httpOnly(httpOnly)
                .secure(secure)
                .domain("")
                .path("/")
                .maxAge(0)
                .sameSite(sameSite);

        if (domain != null && !domain.isBlank()) {
            builder.domain(domain);
        }

        return builder.build();
    }
}
