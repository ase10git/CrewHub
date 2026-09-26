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
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @Mock
    private LoginFailService loginFailService;

    @Mock
    private LoginAttemptService loginAttemptService;

    @Mock
    private HttpServletRequest request;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(
                userRepository,
                passwordEncoder,
                tokenService,
                loginFailService,
                loginAttemptService
        );
    }

    @Test
    void 로그인_성공() {
        LoginRequest request = LoginRequest.builder()
                .email(" test@test.com ")
                .password("Password123!")
                .build();
        User user = createUser();
        LoginFailUser loginFailUser = createLoginFailUser();

        when(loginFailService.checkBlocked("test@test.com"))
                .thenReturn(loginFailUser);
        when(userRepository.findByEmail("test@test.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )).thenReturn(true);

        User result = authService.login(request, this.request);

        assertThat(result).isSameAs(user);
        verify(loginAttemptService).recordLoginAttempt(this.request);
        verify(loginFailService).clear("test@test.com");
        verify(loginFailService, never()).fail(any());
    }

    @Test
    void 존재하지_않는_이메일로_로그인하면_예외가_발생한다() {
        LoginRequest request = LoginRequest.builder()
                .email("test@test.com")
                .password("Password123!")
                .build();
        LoginFailUser loginFailUser = createLoginFailUser();

        when(loginFailService.checkBlocked(request.email()))
                .thenReturn(loginFailUser);
        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request, this.request))
                .isInstanceOf(BusinessException.class)
                .satisfies(exception ->
                        assertThat(((BusinessException) exception).getErrorCode())
                                .isEqualTo(ErrorCode.INVALID_LOGIN)
                );

        verify(loginFailService).fail(loginFailUser);
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    void 잘못된_비밀번호로_로그인하면_예외가_발생한다() {
        LoginRequest request = LoginRequest.builder()
                .email("test@test.com")
                .password("WrongPassword123!")
                .build();
        User user = createUser();
        LoginFailUser loginFailUser = createLoginFailUser();

        when(loginFailService.checkBlocked(request.email()))
                .thenReturn(loginFailUser);
        when(userRepository.findByEmail(request.email()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(
                request.password(),
                user.getPassword()
        )).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request, this.request))
                .isInstanceOf(BusinessException.class)
                .satisfies(exception ->
                        assertThat(((BusinessException) exception).getErrorCode())
                                .isEqualTo(ErrorCode.INVALID_LOGIN)
                );

        verify(loginFailService).fail(loginFailUser);
        verify(loginFailService, never()).clear(any());
    }

    @Test
    void 회원가입_성공() {
        SignUpRequest request = SignUpRequest.builder()
                .email("test@test.com")
                .username("테스트유저")
                .password("Password123!")
                .build();
        User savedUser = createUser();

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(false);
        when(userRepository.existsByUsername(request.username()))
                .thenReturn(false);
        when(passwordEncoder.encode(request.password()))
                .thenReturn("encoded-password");
        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        User result = authService.signup(request);

        assertThat(result).isSameAs(savedUser);
        verify(passwordEncoder).encode(request.password());
        verify(userRepository).save(argThat(user ->
                user.getEmail().equals(request.email())
                        && user.getUsername().equals(request.username())
                        && user.getPassword().equals("encoded-password")
                        && user.getStatus() == UserStatus.ACTIVE
        ));
    }

    @Test
    void 회원가입_이메일이_중복되면_예외가_발생한다() {
        SignUpRequest request = SignUpRequest.builder()
                .email("test@test.com")
                .username("테스트유저")
                .password("Password123!")
                .build();

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(true);

        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(BusinessException.class)
                .satisfies(exception ->
                        assertThat(((BusinessException) exception).getErrorCode())
                                .isEqualTo(ErrorCode.DUPLICATE_EMAIL)
                );

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void 회원가입_닉네임이_중복되면_예외가_발생한다() {
        SignUpRequest request = SignUpRequest.builder()
                .email("test@test.com")
                .username("테스트유저")
                .password("Password123!")
                .build();

        when(userRepository.existsByEmail(request.email()))
                .thenReturn(false);
        when(userRepository.existsByUsername(request.username()))
                .thenReturn(true);

        assertThatThrownBy(() -> authService.signup(request))
                .isInstanceOf(BusinessException.class)
                .satisfies(exception ->
                        assertThat(((BusinessException) exception).getErrorCode())
                                .isEqualTo(ErrorCode.DUPLICATE_USERNAME)
                );

        verify(userRepository, never()).save(any());
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void 로그아웃하면_토큰_블랙리스트_처리를_위임한다() {
        String accessToken = "access-token";

        authService.logout(accessToken);

        verify(tokenService).saveBlacklistAndMarkRefreshTokenRevoked(accessToken);
    }

    private User createUser() {
        return User.builder()
                .id(1)
                .username("테스트유저")
                .email("test@test.com")
                .password("encoded-password")
                .status(UserStatus.ACTIVE)
                .build();
    }

    private LoginFailUser createLoginFailUser() {
        return LoginFailUser.builder()
                .email("test@test.com")
                .failCount(0)
                .ttl(300)
                .build();
    }
}
