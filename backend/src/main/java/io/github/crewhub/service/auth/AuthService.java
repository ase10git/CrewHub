package io.github.crewhub.service.auth;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.auth.request.LoginRequest;
import io.github.crewhub.dto.auth.request.SignUpRequest;
import io.github.crewhub.entity.auth.LoginFailUser;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.enums.user.UserStatus;
import io.github.crewhub.repository.user.UserRepository;
import io.github.crewhub.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 인증 인가 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenService tokenService;
    private final LoginFailService loginFailService;

    public User login(LoginRequest request) {
        String email = request.email();

        LoginFailUser loginFailUser = loginFailService.checkBlocked(email);

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            loginFailService.fail(loginFailUser);
            throw new BusinessException(ErrorCode.INVALID_LOGIN);
        }

        validatePassword(
                email,
                request.password(),
                user.getPassword(),
                loginFailUser
        );

        return user;
    }

    @Transactional
    public User signup(SignUpRequest request) {
        validateDuplicateUser(request);

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(
                        request.password()
                ))
                .status(UserStatus.ACTIVE)
                .build();

        return userRepository.save(user);
    }

    private void validateDuplicateUser(SignUpRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EMAIL);
        }
        if (userRepository.existsByUsername(request.username())) {
            throw new BusinessException(ErrorCode.DUPLICATE_USERNAME);
        }
    }

    private void validatePassword(
            String email,
            String rawPassword,
            String encodedPassword,
            LoginFailUser loginFailUser
    ) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            loginFailService.fail(loginFailUser);
            throw new BusinessException(ErrorCode.INVALID_LOGIN);
        }
        loginFailService.clear(email);
    }

    @Transactional
    public void logout(String accessToken) {
        tokenService.saveBlacklistAndMarkRefreshTokenRevoked(accessToken);
    }
}
