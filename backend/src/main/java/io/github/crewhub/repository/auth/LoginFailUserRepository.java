package io.github.crewhub.repository.auth;

import io.github.crewhub.entity.auth.LoginFailUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * LoginFailUser 관리용 Repository
 */
@Repository
public interface LoginFailUserRepository extends CrudRepository<LoginFailUser, String> {
}
