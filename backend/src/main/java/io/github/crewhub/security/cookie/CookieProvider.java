package io.github.crewhub.security.cookie;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.token.CsrfTokenInfo;
import io.github.crewhub.dto.token.RefreshTokenInfo;
import io.github.crewhub.enums.common.ErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * Cookie 생성 및 추출
 * CSRF Token과 연관된 Header 추출 관리
 */
@Component
public class CookieProvider {

    @Value("${cookie.refresh-token.secure}")
    private boolean refreshTokenSecure;

    @Value("${cookie.refresh-token.http-only}")
    private boolean refreshTokenHttpOnly;

    @Value("${cookie.refresh-token.domain}")
    private String refreshTokenDomain;

    @Value("${cookie.refresh-token.same-site}")
    private String refreshTokenSameSite;

    @Value("${cookie.csrf-token.secure}")
    private boolean csrfTokenSecure;

    @Value("${cookie.csrf-token.http-only}")
    private boolean csrfTokenHttpOnly;

    @Value("${cookie.csrf-token.domain}")
    private String csrfTokenDomain;

    @Value("${cookie.csrf-token.same-site}")
    private String csrfTokenSameSite;

    private static final String REFRESH_TOKEN = "refreshToken";
    private static final String CSRF_TOKEN = "csrfToken";
    private static final String CSRF_HEADER = "X-CSRF-TOKEN";

    private ResponseCookie.ResponseCookieBuilder refreshTokenCookieBuilder(
            String refreshToken, Duration maxAge
    ) {

        ResponseCookie.ResponseCookieBuilder builder =
                ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .httpOnly(refreshTokenHttpOnly)
                .secure(refreshTokenSecure)
                .path("/api/auth/refresh")
                .domain("")
                .maxAge(maxAge)
                .sameSite(refreshTokenSameSite);

        if (refreshTokenDomain != null && !refreshTokenDomain.isBlank()) {
            builder.domain(refreshTokenDomain);
        }

        return builder;
    }

    public ResponseCookie createRefreshTokenCookie(RefreshTokenInfo refreshTokenInfo) {
        return refreshTokenCookieBuilder(
                refreshTokenInfo.refreshToken(),
                Duration.ofSeconds(refreshTokenInfo.ttlSeconds())
        ).build();
    }

    public String extractRefreshTokenCookie(HttpServletRequest request) {
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

    private ResponseCookie.ResponseCookieBuilder csrfTokenCookieBuilder(
            String csrfToken, Duration maxAge
    ) {

        ResponseCookie.ResponseCookieBuilder builder =
                ResponseCookie.from(CSRF_TOKEN, csrfToken)
                        .httpOnly(csrfTokenHttpOnly)
                        .secure(csrfTokenSecure)
                        .path("/api/auth/refresh")
                        .domain("")
                        .maxAge(maxAge)
                        .sameSite(csrfTokenSameSite);

        if (csrfTokenDomain != null && !csrfTokenDomain.isBlank()) {
            builder.domain(csrfTokenDomain);
        }

        return builder;
    }

    public ResponseCookie createCsrfTokenCookie(CsrfTokenInfo csrfTokenInfo) {
        return csrfTokenCookieBuilder(
                csrfTokenInfo.csrfToken(),
                Duration.ofSeconds(csrfTokenInfo.ttlSeconds())
        ).build();
    }

    public String extractCsrfTokenCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new BusinessException(ErrorCode.CSRF_TOKEN_NOT_FOUND);
        }

        for (Cookie cookie : cookies) {
            if (CSRF_TOKEN.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        throw new BusinessException(ErrorCode.CSRF_TOKEN_NOT_FOUND);
    }

    public String extractCsrfHeader(HttpServletRequest request) {
        String csrfToken = request.getHeader(CSRF_HEADER);

        if (csrfToken == null || csrfToken.isBlank()) {
            throw new BusinessException(ErrorCode.CSRF_TOKEN_NOT_FOUND);
        }

        return csrfToken;
    }

    public ResponseCookie deleteCsrfTokenCookie() {
        return csrfTokenCookieBuilder(
                "", Duration.ZERO
        ).build();
    }
}
