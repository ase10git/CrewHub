package io.github.crewhub.security.jwt;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.security.details.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
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

    public String generateAccessToken(CustomUserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(
                "email",
                userDetails.getEmail()
        );

        claims.put(
                "username",
                userDetails.getNickname()
        );

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + accessTokenExpiration
        );

        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(userDetails.getUserId()))
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(Integer userId) {
        Map<String, Object> claims = new HashMap<>();

        claims.put(
                "type",
                "refresh"
        );

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + refreshTokenExpiration
        );

        return Jwts.builder()
                .claims(claims)
                .subject(String.valueOf(userId))
                .id(UUID.randomUUID().toString())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
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

    public Claims parseRefreshToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
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

    public void validateAccessToken(
            String token,
            UserDetails userDetails
    ) {
        Claims claims = parseAccessToken(token);
        String userId = extractUserId(claims);

        if (!userId.equals(userDetails.getUsername())) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    public void validateAccessToken(String token) {
        Claims claims = parseAccessToken(token);

        if (claims.getSubject() == null) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    public void validateRefreshToken(String token) {
        Claims claims = parseRefreshToken(token);

        String type = extractTokenType(claims);

        if (!"refresh".equals(type)) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (claims.getSubject() == null) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (claims.getId() == null) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public Date extractExpiration(Claims claims) {
        return claims.getExpiration();
    }

    public Date extractIssuedAt(Claims claims) {
        return claims.getIssuedAt();
    }

    public Long getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
