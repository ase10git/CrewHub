package io.github.crewhub.enums.user;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 사용자 상태
 */
@Schema(
        description = """
                사용자 계정 상태
                
                * ACTIVE - 활성 상태
                * INACTIVE - 비활성 상태
                * BLOCKED - 차단 상태
                """
)
public enum UserStatus {
    @Schema(description = "활성 사용자")
    ACTIVE,

    @Schema(description = "비활성 사용자")
    INACTIVE,

    @Schema(description = "차단된 사용자")
    BLOCKED
}
