package io.github.crewhub.entity.auth;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

/**
 * JWT Refresh Token
 * Redis 저장
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@RedisHash("refresh_token")
public class RefreshToken {

    @Id
    private Integer userId;

    @Indexed
    private String refreshTokenHash;

    private LocalDateTime expiredAt;

    private LocalDateTime issuedAt;

    private String jti;

    // 초 단위
    @TimeToLive
    private Long ttl;
}
