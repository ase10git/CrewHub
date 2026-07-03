package io.github.crewhub.service.auth;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.auth.request.LoginRequest;
import io.github.crewhub.dto.auth.request.SignUpRequest;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.enums.user.UserStatus;
import io.github.crewhub.repository.user.UserRepository;
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

    public User login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElseThrow(
                () -> new BusinessException(ErrorCode.INVALID_LOGIN)
        );

        validatePassword(request.password(), user.getPassword());

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

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new BusinessException(ErrorCode.INVALID_LOGIN);
        }
    }
}
