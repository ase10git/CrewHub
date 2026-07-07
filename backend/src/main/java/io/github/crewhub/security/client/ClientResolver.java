package io.github.crewhub.security.client;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * request에서 클라이언트 정보를 추출하는 클래스
 */
@Component
public class ClientResolver {

    public String resolveIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-for");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
}
