package io.github.crewhub.repository.token;

import io.github.crewhub.entity.token.AccessTokenBlacklist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Access Token Blacklist 관리용 Repository
 */
@Repository
public interface AccessTokenBlacklistRepository extends CrudRepository<AccessTokenBlacklist, String> {
}
