package io.github.crewhub.dto.gathering.response;

import io.github.crewhub.enums.gathering.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 모임 회원 정보 응답 데이터
 */
@Schema(description = "모임 회원 정보 응답")
@Builder
public record GatheringMemberResponse(
        @Schema(
                description = "회원 ID",
                example = "10"
        )
        Integer userId,

        @Schema(
                description = "회원 닉네임",
                example = "crewhub_user"
        )
        String username,

        @Schema(
                description = "회원 역할",
                example = "MEMBER"
        )
        MemberRole role,

        @Schema(
                description = "가입일",
                example = "2026-06-25T10:30:00"
        )
        LocalDateTime createdAt,

        @Schema(
                description = "최근 수정일",
                example = "2026-06-25T11:00:00"
        )
        LocalDateTime updatedAt
) {
}
