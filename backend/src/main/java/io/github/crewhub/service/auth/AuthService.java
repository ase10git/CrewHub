package io.github.crewhub.service.auth;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.auth.request.LoginRequest;
import io.github.crewhub.dto.auth.request.SignUpRequest;
import io.github.crewhub.dto.auth.response.LoginResponse;
import io.github.crewhub.dto.auth.response.LoginResult;
import io.github.crewhub.dto.auth.response.SignUpResponse;
import io.github.crewhub.dto.auth.response.SignUpResult;
import io.github.crewhub.entity.auth.RefreshToken;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.enums.user.UserStatus;
import io.github.crewhub.repository.token.TokenRepository;
import io.github.crewhub.repository.user.UserRepository;
import io.github.crewhub.security.details.CustomUserDetails;
import io.github.crewhub.security.jwt.JwtProvider;
import io.github.crewhub.utils.DateUtils;
import io.github.crewhub.utils.TokenHashUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * 인증 인가 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    private final DateUtils dateUtils;
    private final TokenHashUtils tokenHashUtils;

    public LoginResult login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(
                () -> new BusinessException(ErrorCode.INVALID_LOGIN)
        );

        validatePassword(request.password(), user.getPassword());

        String accessToken = jwtProvider.generateAccessToken(new CustomUserDetails(user));

        String refreshToken = jwtProvider.generateRefreshToken(user.getId());

        saveRefreshToken(user.getId(), refreshToken);

        LoginResponse response = LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .accessToken(accessToken)
                .build();

        return LoginResult.builder()
                .loginResponse(response)
                .refreshToken(refreshToken)
                .build();
    }

    @Transactional
    public SignUpResult signup(SignUpRequest request) {
        validateDuplicateUser(request);

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(
                        request.password()
                ))
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        String accessToken = jwtProvider.generateAccessToken(new CustomUserDetails(savedUser));

        String refreshToken = jwtProvider.generateRefreshToken(savedUser.getId());

        saveRefreshToken(user.getId(), refreshToken);

        SignUpResponse signUpResponse = SignUpResponse.builder()
                .userId(savedUser.getId())
                .username(savedUser.getUsername())
                .accessToken(accessToken)
                .build();

        return SignUpResult.builder()
                .signUpResponse(signUpResponse)
                .refreshToken(refreshToken)
                .build();
    }

    private void validateDuplicateUser(SignUpRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
        }
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BusinessException(ErrorCode.INVALID_LOGIN);
        }
    }

    private void saveRefreshToken(Integer userId, String refreshToken) {
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

        RefreshToken token = RefreshToken.builder()
                .userId(userId)
                .refreshTokenHash(hashedRefreshToken)
                .issuedAt(issuedAt)
                .jti(claims.getId())
                .expiredAt(expiredAt)
                .ttl(ttl)
                .build();

        tokenRepository.deleteById(String.valueOf(userId));

        tokenRepository.save(token);
    }
}
