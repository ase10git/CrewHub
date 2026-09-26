package io.github.crewhub.common.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * HTTP 요청과 응답을 기록하는 공통 로깅 필터
 */
public class LoggingFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String REQUEST_ID = "requestId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String requestId = UUID.randomUUID().toString();
        long startTime = System.currentTimeMillis();

        MDC.put(REQUEST_ID, requestId);
        response.setHeader("X-Request-Id", requestId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            String userId = authentication != null && authentication.isAuthenticated()
                    ? authentication.getName()
                    : "anonymous";

            log.info(
                    "HTTP request completed: method={}, uri={}, status={}, duration={}ms, userId={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration,
                    userId
            );

            MDC.remove(REQUEST_ID);
        }
    }
}
