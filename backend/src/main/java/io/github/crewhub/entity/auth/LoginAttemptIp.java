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
@RedisHash("login_attempt_ip")
public class LoginAttemptIp {
    @Id
    private String ip;

    private int attemptCount;

    @TimeToLive
    private long ttl;

    public void increase() {
        attemptCount++;
    }

    public boolean isBlocked(int maxCount) {
        return attemptCount >= maxCount;
    }
}
