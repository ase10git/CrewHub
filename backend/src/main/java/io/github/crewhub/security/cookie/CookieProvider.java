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
import java.time.Instant;

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

    private String REFRESH_TOKEN = "refreshToken";

    public ResponseCookie createRefreshTokenCookie(RefreshTokenInfo refreshTokenInfo) {
        ResponseCookie.ResponseCookieBuilder builder =
                ResponseCookie.from(REFRESH_TOKEN, refreshTokenInfo.refreshToken())
                .httpOnly(httpOnly)
                .secure(secure)
                .domain("")
                .path("/")
                .maxAge(
                        Duration.between(
                                Instant.now(),
                                refreshTokenInfo.expiresAt()
                        )
                )
                .sameSite(sameSite);

        if (domain != null && !domain.isBlank()) {
            builder.domain(domain);
        }

        return builder.build();
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
        ResponseCookie.ResponseCookieBuilder builder =
                ResponseCookie.from(REFRESH_TOKEN, "")
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
