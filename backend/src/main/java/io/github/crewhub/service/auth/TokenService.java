package io.github.crewhub.service.auth;

import io.github.crewhub.dto.auth.response.AuthResponse;
import io.github.crewhub.dto.auth.response.AuthResult;
import io.github.crewhub.dto.token.RefreshTokenInfo;
import io.github.crewhub.entity.auth.RefreshToken;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.repository.token.TokenRepository;
import io.github.crewhub.security.jwt.JwtProvider;
import io.github.crewhub.utils.DateUtils;
import io.github.crewhub.utils.TokenHashUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Token 발급, 저장, 관리 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;

    private final DateUtils dateUtils;
    private final TokenHashUtils tokenHashUtils;

    @Transactional
    public AuthResult issueAccessToken(User user) {
        String accessToken = jwtProvider.generateAccessToken(user);

        RefreshTokenInfo refreshTokenInfo = jwtProvider.generateRefreshToken(user.getId());

        saveRefreshToken(user.getId(), refreshTokenInfo);

        AuthResponse authResponse = AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .accessToken(accessToken)
                .build();

        return AuthResult.builder()
                .authResponse(authResponse)
                .refreshTokenInfo(refreshTokenInfo)
                .build();
    }

    private RefreshToken createRefreshTokenEntity(Integer userId, RefreshTokenInfo refreshTokenInfo) {
        String refreshToken = refreshTokenInfo.refreshToken();

        String hashedRefreshToken = tokenHashUtils.hash(refreshToken);

        Claims claims = jwtProvider.parseRefreshToken(refreshToken);

        LocalDateTime issuedAt = dateUtils.toLocalDateTime(
                jwtProvider.extractIssuedAt(claims)
        );
        LocalDateTime expiredAt = dateUtils.toLocalDateTime(
                jwtProvider.extractExpiration(claims)
        );

        Long ttl = TimeUnit.MILLISECONDS.toSeconds(
                jwtProvider.getRefreshTokenExpiration()
        );

        return RefreshToken.builder()
                .userId(userId)
                .refreshTokenHash(hashedRefreshToken)
                .issuedAt(issuedAt)
                .jti(claims.getId())
                .expiredAt(expiredAt)
                .ttl(ttl)
                .build();
    }

    private void saveRefreshToken(Integer userId, RefreshTokenInfo refreshTokenInfo) {
        RefreshToken token = createRefreshTokenEntity(userId, refreshTokenInfo);

        tokenRepository.deleteById(String.valueOf(userId));

        tokenRepository.save(token);
    }
}
