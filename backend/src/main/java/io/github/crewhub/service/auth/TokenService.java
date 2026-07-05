package io.github.crewhub.service.auth;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.auth.response.AuthResponse;
import io.github.crewhub.dto.auth.response.AuthResult;
import io.github.crewhub.dto.token.RefreshTokenInfo;
import io.github.crewhub.entity.auth.AccessTokenBlacklist;
import io.github.crewhub.entity.auth.RefreshToken;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.repository.token.AccessTokenBlacklistRepository;
import io.github.crewhub.repository.token.TokenRepository;
import io.github.crewhub.security.jwt.JwtProvider;
import io.github.crewhub.service.user.UserService;
import io.github.crewhub.utils.DateUtils;
import io.github.crewhub.utils.TokenHashUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Token 발급, 저장, 관리 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {
    private final TokenRepository tokenRepository;
    private final AccessTokenBlacklistRepository blacklistRepository;

    private final UserService userService;

    private final JwtProvider jwtProvider;

    private final DateUtils dateUtils;
    private final TokenHashUtils tokenHashUtils;

    public AuthResult issueTokens(User user) {
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

        LocalDateTime issuedAt = dateUtils.toLocalDateTime(
                refreshTokenInfo.issuedAt()
        );
        LocalDateTime expiredAt = dateUtils.toLocalDateTime(
                refreshTokenInfo.expiresAt()
        );

        Long ttl = refreshTokenInfo.ttlSeconds();

        return RefreshToken.builder()
                .userId(userId)
                .refreshTokenHash(hashedRefreshToken)
                .issuedAt(issuedAt)
                .jti(refreshTokenInfo.jti())
                .expiredAt(expiredAt)
                .ttl(ttl)
                .build();
    }

    @Transactional
    public void saveRefreshToken(Integer userId, RefreshTokenInfo refreshTokenInfo) {
        RefreshToken token = createRefreshTokenEntity(userId, refreshTokenInfo);

        tokenRepository.save(token);
    }

    public RefreshToken validateRefreshToken(String refreshToken) {
        Claims claims = jwtProvider.parseAndValidateRefreshToken(refreshToken);

        String userId = jwtProvider.extractUserId(claims);

        RefreshToken savedToken = tokenRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.INVALID_REFRESH_TOKEN
                ));

        if (!tokenHashUtils.matches(
                refreshToken, savedToken.getRefreshTokenHash()
        )) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (!savedToken.getJti().equals(
                jwtProvider.extractJti(claims)
        )) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (savedToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        return savedToken;
    }

    public AuthResult refresh(String refreshToken) {
        RefreshToken refreshTokenEntity = validateRefreshToken(refreshToken);

        User user = userService.getUser(refreshTokenEntity.getUserId());

        return issueTokens(user);
    }

    @Transactional
    public void saveBlacklistAndDeleteRefreshToken(String accessToken) {
        Claims claims = jwtProvider.parseAndValidateAccessToken(accessToken);
        saveAccessTokenBlacklist(claims);

        String userId = jwtProvider.extractUserId(claims);
        deleteRefreshToken(userId);
    }

    @Transactional
    public void deleteRefreshToken(String userId) {
        tokenRepository.deleteById(userId);
    }

    public void checkAccessTokenBlacklist(String jti) {
        if (blacklistRepository.existsById(jti)) {
            throw new BusinessException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    @Transactional
    public void saveAccessTokenBlacklist(Claims claims) {
        AccessTokenBlacklist blacklist = AccessTokenBlacklist.builder()
                .jti(jwtProvider.extractJti(claims))
                .ttl(jwtProvider.getRemainingAccessTokenTtl(claims))
                .build();

        blacklistRepository.save(blacklist);
    }
}
