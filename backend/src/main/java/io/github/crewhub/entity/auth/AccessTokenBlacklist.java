package io.github.crewhub.entity.auth;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * JWT Access Token 블랙리스트
 * Redis 저장
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@RedisHash("access_token_blacklist")
public class AccessTokenBlacklist {
    @Id
    private String jti;

    @TimeToLive
    private Long ttl;
}
