package io.github.crewhub.enums.token;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Refresh Token의 상태 목록
 */
@Schema(
        description = """
                Refresh Token의 상태
                
                * ACTIVE - 활성 상태
                * USED - 사용된 상태
                * REVOKED - 만료 상태
                """
)
public enum RefreshTokenStatus {
    ACTIVE,
    USED,
    REVOKED
}
