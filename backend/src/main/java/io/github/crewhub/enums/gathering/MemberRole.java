package io.github.crewhub.enums.gathering;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 모임 회원의 역할
 */
@Schema(
        description = """
                모임 회원 역할
                
                * MANAGER - 모임 관리자
                * MEMBER - 일반 회원
                """
)
public enum MemberRole {
    @Schema(description = "모임 관리자")
    MANAGER,

    @Schema(description = "일반 회원")
    MEMBER
}
