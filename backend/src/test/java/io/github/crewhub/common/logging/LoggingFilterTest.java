package io.github.crewhub.common.logging;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockFilterChain;
import org.slf4j.MDC;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class LoggingFilterTest {
    private final LoggingFilter loggingFilter = new LoggingFilter();

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void 요청을_처리하고_request_id를_응답_헤더에_추가한다()
            throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/gathering");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        loggingFilter.doFilter(request, response, filterChain);

        assertNotNull(response.getHeader("X-Request-Id"));
        assertEquals(request, filterChain.getRequest());
    }
}
