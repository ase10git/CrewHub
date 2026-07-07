package io.github.crewhub.repository.token;

import io.github.crewhub.entity.token.RefreshToken;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Refresh Token 관리를 위한 Redis Repository
 * 객체(Entity) 중심의 CRUD 관리 시 사용
 */
@Repository
public interface RefreshTokenRepository extends ListCrudRepository<RefreshToken, String> {
    List<RefreshToken> findAllByFamilyId(String familyId);
}
