package io.github.crewhub.interceptor;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.security.details.CustomUserDetailsService;
import io.github.crewhub.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * JWT 전달을 위한 Connect interceptor
 */
@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(
            Message<?> message,
            MessageChannel channel
    ) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String bearerToken = accessor.getFirstNativeHeader("Authorization");

            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                throw new BusinessException(ErrorCode.INVALID_TOKEN);
            }

            String token = bearerToken.substring(7);

            if (!jwtProvider.isTokenValid(token)) {
                throw new BusinessException(ErrorCode.INVALID_TOKEN);
            }

            Integer userId = Integer.valueOf(jwtProvider.extractUserId(token));

            UserDetails userDetails = userDetailsService.loadByUserId(userId);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );

            accessor.setUser(authentication);
        }

        return message;
    }
}
