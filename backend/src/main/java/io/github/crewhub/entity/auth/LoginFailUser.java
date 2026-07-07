package io.github.crewhub.entity.auth;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

/**
 * 로그인 실패 횟수 카운트
 * Redis 저장
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@RedisHash("login_fail_user")
public class LoginFailUser {
    @Id
    private String email;

    private int failCount;

    @TimeToLive
    private long ttl;

    public void increase() {
        failCount++;
    }

    public boolean isBlocked(int maxCount) {
        return failCount >= maxCount;
    }
}
