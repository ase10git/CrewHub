package io.github.crewhub.service.auth;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.auth.response.AuthResponse;
import io.github.crewhub.dto.auth.response.AuthResult;
import io.github.crewhub.dto.token.CsrfTokenInfo;
import io.github.crewhub.dto.token.RefreshTokenInfo;
import io.github.crewhub.entity.auth.AccessTokenBlacklist;
import io.github.crewhub.entity.auth.RefreshToken;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.enums.token.RefreshTokenStatus;
import io.github.crewhub.repository.token.AccessTokenBlacklistRepository;
import io.github.crewhub.repository.token.RefreshTokenRepository;
import io.github.crewhub.security.cookie.CookieProvider;
import io.github.crewhub.security.csrf.CsrfTokenProvider;
import io.github.crewhub.security.jwt.JwtProvider;
import io.github.crewhub.service.user.UserService;
import io.github.crewhub.utils.DateUtils;
import io.github.crewhub.utils.TokenHashUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Token 발급, 저장, 관리 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final AccessTokenBlacklistRepository blacklistRepository;

    private final UserService userService;

    private final JwtProvider jwtProvider;
    private final CsrfTokenProvider csrfTokenProvider;
    private final CookieProvider cookieProvider;

    private final DateUtils dateUtils;
    private final TokenHashUtils tokenHashUtils;

    private final RedissonClient redissonClient;

    private static final String REFRESH_LOCK_KEY_PREFIX = "refresh-lock:";

    public AuthResult issueTokens(User user, String familyId) {
        String accessToken = jwtProvider.generateAccessToken(user);

        RefreshTokenInfo refreshTokenInfo = jwtProvider.generateRefreshToken(user.getId());

        saveRefreshToken(user.getId(), familyId, refreshTokenInfo);

        CsrfTokenInfo csrfTokenInfo = csrfTokenProvider.generate(refreshTokenInfo);

        AuthResponse authResponse = AuthResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .accessToken(accessToken)
                .build();

        return AuthResult.builder()
                .authResponse(authResponse)
                .refreshTokenInfo(refreshTokenInfo)
                .csrfTokenInfo(csrfTokenInfo)
                .build();
    }

    public AuthResult issueFirstTokens(User user) {
        String refreshTokenFamilyId = UUID.randomUUID().toString();

        return issueTokens(user, refreshTokenFamilyId);
    }

    private RefreshToken createRefreshTokenEntity(
            Integer userId,
            String familyId,
            RefreshTokenInfo refreshTokenInfo
    ) {
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
                .jti(refreshTokenInfo.jti())
                .userId(userId)
                .familyId(familyId)
                .refreshTokenHash(hashedRefreshToken)
                .status(RefreshTokenStatus.ACTIVE)
                .issuedAt(issuedAt)
                .expiredAt(expiredAt)
                .ttl(ttl)
                .build();
    }

    @Transactional
    public void saveRefreshToken(
            Integer userId,
            String familyId,
            RefreshTokenInfo refreshTokenInfo
    ) {
        RefreshToken token = createRefreshTokenEntity(userId, familyId, refreshTokenInfo);

        refreshTokenRepository.save(token);
    }

    public RefreshToken validateRefreshToken(RefreshToken savedToken, String refreshToken) {
        if (savedToken.getStatus()
                .equals(RefreshTokenStatus.USED)) {
            revokeAllFamilyRefreshToken(savedToken.getFamilyId());

            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (savedToken.getStatus()
                .equals(RefreshTokenStatus.REVOKED)) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (!tokenHashUtils.matches(
                refreshToken, savedToken.getRefreshTokenHash()
        )) {
            throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        if (savedToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }

        return savedToken;
    }

    @Transactional
    public AuthResult refresh(HttpServletRequest request) {
        String refreshToken = cookieProvider.extractRefreshTokenCookie(request);

        Claims claims = jwtProvider.parseAndValidateRefreshToken(refreshToken);

        String jti = jwtProvider.extractJti(claims);

        validateCsrfToken(request, jti);

        RLock lock = redissonClient.getLock(REFRESH_LOCK_KEY_PREFIX + jti);

        boolean locked = false;

        try {
            locked = lock.tryLock(3, 10, TimeUnit.SECONDS);

            if (!locked) {
                throw new BusinessException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            RefreshToken savedToken = refreshTokenRepository.findById(jti)
                    .orElseThrow(() -> new BusinessException(
                            ErrorCode.INVALID_REFRESH_TOKEN
                    ));

            validateRefreshToken(savedToken, refreshToken);

            User user = userService.getUser(savedToken.getUserId());

            savedToken.changeStatus(RefreshTokenStatus.USED);
            refreshTokenRepository.save(savedToken);

            return issueTokens(user, savedToken.getFamilyId());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

    }

    @Transactional
    public void saveBlacklistAndMarkRefreshTokenRevoked(String accessToken) {
        Claims claims = jwtProvider.parseAndValidateAccessToken(accessToken);
        saveAccessTokenBlacklist(claims);

        String jti = jwtProvider.extractJti(claims);
        markRefreshTokenRevoked(jti);
    }

    @Transactional
    public void markRefreshTokenRevoked(String jti) {
        refreshTokenRepository.findById(jti)
                .ifPresent(
                        token -> {
                            token.changeStatus(RefreshTokenStatus.REVOKED);
                            refreshTokenRepository.save(token);
                        }
                );
    }

    @Transactional
    public void revokeAllFamilyRefreshToken(String familyId) {
        refreshTokenRepository.findAllByFamilyId(familyId)
                .forEach(token -> {
                    if (!token.getStatus().equals(RefreshTokenStatus.REVOKED)) {
                        token.changeStatus(RefreshTokenStatus.REVOKED);
                    }
                    refreshTokenRepository.save(token);
                });
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

    public void validateCsrfToken(HttpServletRequest request, String jti) {
        String cookieToken = cookieProvider.extractCsrfTokenCookie(request);
        String headerToken = cookieProvider.extractCsrfHeader(request);

        csrfTokenProvider.validateCsrfToken(cookieToken, headerToken, jti);
    }
}
