package io.github.crewhub.security.jwt;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.token.RefreshTokenInfo;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.common.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

/**
 * JWT Token 생성 및 검증
 */
@Component
@RequiredArgsConstructor
public class JwtProvider {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.access-token-expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private Long refreshTokenExpiration;

    private static final String BEARER_TOKEN_PREFIX = "Bearer ";

    public String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(
                "email",
                user.getEmail()
        );

        claims.put(
                "username",
                user.getUsername()
        );

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + accessTokenExpiration
        );

        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(user.getId()))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public RefreshTokenInfo generateRefreshToken(Integer userId) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(
                "type",
                "refresh"
        );

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + refreshTokenExpiration
        );

        String jti = UUID.randomUUID().toString();

        String refreshToken = Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(userId))
                .id(jti)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();

        return RefreshTokenInfo.builder()
                .refreshToken(refreshToken)
                .jti(jti)
                .expiresAt(expiration.toInstant())
                .issuedAt(now.toInstant())
                .ttlSeconds(refreshTokenExpiration/1000)
                .build();
    }

    public String extractUserId(Claims claims) {
        return claims.getSubject();
    }

    public String extractAccessTokenUserId(String token) {
        return parseAccessToken(token).getSubject();
    }

    public String extractJti(Claims claims) {
        return claims.getId();
    }

    public String extractTokenType(Claims claims) {
        return claims.get("type", String.class);
    }

    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver
    ) {
        Claims claims = parseAccessToken(token);

        return resolver.apply(claims);
    }

    private Claims parseAccessToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (MalformedJwtException |
                UnsupportedJwtException |
                SecurityException |
                IllegalArgumentException e
        ) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    public Claims parseAndValidateRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String type = extractTokenType(claims);

            if (!"refresh".equals(type)) {
                throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            String userId = claims.getSubject();

            if (userId == null || userId.isBlank()) {
                throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            String jti = claims.getId();

            if (jti == null || jti.isBlank()) {
                throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            return claims;
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        } catch (MalformedJwtException |
                 UnsupportedJwtException |
                 SecurityException |
                 IllegalArgumentException e
        ) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public Claims parseAndValidateAccessToken(String token) {
        Claims claims = parseAccessToken(token);

        if (claims.getSubject() == null) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        return claims;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractBearerTokenAndUserId(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        if (!authorizationHeader.startsWith(BEARER_TOKEN_PREFIX)) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        String accessToken = authorizationHeader.substring(BEARER_TOKEN_PREFIX.length());

        if (accessToken.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        }

        Claims claims = parseAndValidateAccessToken(accessToken);

        return claims.getSubject();
    }
}
