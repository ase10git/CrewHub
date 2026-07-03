package io.github.crewhub.security.cookie;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.token.RefreshTokenInfo;
import io.github.crewhub.enums.common.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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

    private static final String REFRESH_TOKEN = "refreshToken";

    private ResponseCookie.ResponseCookieBuilder refreshTokenCookieBuilder(
            String refreshToken, Duration maxAge
    ) {
        ResponseCookie.ResponseCookieBuilder builder =
                ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .httpOnly(httpOnly)
                .secure(secure)
                .domain("")
                .path("/")
                .maxAge(maxAge)
                .sameSite(sameSite);

        if (domain != null && !domain.isBlank()) {
            builder.domain(domain);
        }

        return builder;
    }

    public ResponseCookie createRefreshTokenCookie(RefreshTokenInfo refreshTokenInfo) {
        return refreshTokenCookieBuilder(
                refreshTokenInfo.refreshToken(),
                Duration.ofSeconds(refreshTokenInfo.ttlSeconds())
        ).build();
    }

    public String extractRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        for (Cookie cookie : cookies) {
            if (REFRESH_TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new BusinessException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }

    public ResponseCookie deleteRefreshTokenCookie() {
        return refreshTokenCookieBuilder(
                "", Duration.ZERO
        ).build();
    }
}
