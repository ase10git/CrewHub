package io.github.crewhub.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.crewhub.common.response.ErrorResponse;
import io.github.crewhub.enums.common.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Jwt인증 진입 설정 및 예외 처리
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        ErrorCode errorCode = ErrorCode.INVALID_TOKEN;

        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        ErrorResponse body = ErrorResponse.builder()
                .success(false)
                .message(errorCode.getMessage())
                .code(errorCode.name())
                .build();

        objectMapper.writeValue(
                response.getWriter(),
                body
        );
    }
}
