package io.github.crewhub.config.jpa;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * JPA Auditing 허용 설정
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
