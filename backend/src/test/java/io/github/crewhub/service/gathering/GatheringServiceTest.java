package io.github.crewhub.service.gathering;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.gathering.request.CreateGatheringRequest;
import io.github.crewhub.dto.gathering.request.UpdateGatheringRequest;
import io.github.crewhub.dto.gathering.response.GatheringDetailResponse;
import io.github.crewhub.dto.gathering.response.GatheringSummaryResponse;
import io.github.crewhub.entity.chat.ChatRoom;
import io.github.crewhub.entity.gathering.Gathering;
import io.github.crewhub.entity.gathering.GatheringCategory;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.enums.gathering.MemberRole;
import io.github.crewhub.enums.user.UserStatus;
import io.github.crewhub.repository.chat.ChatRoomRepository;
import io.github.crewhub.repository.gathering.GatheringCategoryRepository;
import io.github.crewhub.repository.gathering.GatheringMemberRepository;
import io.github.crewhub.repository.gathering.GatheringRepository;
import io.github.crewhub.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GatheringServiceTest {

    @Mock
    private GatheringRepository gatheringRepository;

    @Mock
    private GatheringCategoryRepository categoryRepository;

    @Mock
    private GatheringMemberRepository memberRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    private GatheringService gatheringService;

    @BeforeEach
    void setUp() {
        gatheringService = new GatheringService(
                gatheringRepository,
                categoryRepository,
                memberRepository,
                userRepository,
                chatRoomRepository
        );
    }

    @Test
    void 모임_상세_조회_성공() {
        Gathering gathering = createGathering();

        when(gatheringRepository.findDetailById(1))
                .thenReturn(Optional.of(gathering));

        GatheringDetailResponse result = gatheringService.getGathering(1);

        assertThat(result.gatheringId()).isEqualTo(1);
        assertThat(result.gatheringName()).isEqualTo("백엔드 스터디");
        assertThat(result.categoryLabel()).isEqualTo("개발");
        assertThat(result.managerId()).isEqualTo(10);
        assertThat(result.managerName()).isEqualTo("관리자");
    }

    @Test
    void 존재하지_않는_모임을_조회하면_예외가_발생한다() {
        when(gatheringRepository.findDetailById(999))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> gatheringService.getGathering(999))
                .isInstanceOf(BusinessException.class)
                .satisfies(exception ->
                        assertThat(((BusinessException) exception).getErrorCode())
                                .isEqualTo(ErrorCode.GATHERING_NOT_FOUND)
                );
    }

    @Test
    void 이름으로_모임을_검색하면_조회_결과를_응답으로_변환한다() {
        Gathering gathering = createGathering();

        when(gatheringRepository.findByGatheringName(
                eq("스터디"),
                any(PageRequest.class)
        )).thenReturn(new PageImpl<>(
                List.of(gathering),
                PageRequest.of(0, 10),
                1
        ));

        var result = gatheringService.searchByName("스터디", 0, 10);

        assertThat(result.content())
                .singleElement()
                .extracting(
                        GatheringSummaryResponse::gatheringName,
                        GatheringSummaryResponse::categoryLabel,
                        GatheringSummaryResponse::managerName
                )
                .containsExactly("백엔드 스터디", "개발", "관리자");
    }

    @Test
    void 빈_검색어로_검색하면_예외가_발생하고_저장소를_호출하지_않는다() {
        assertThatThrownBy(() -> gatheringService.searchByName(" ", 0, 10))
                .isInstanceOf(BusinessException.class)
                .satisfies(exception ->
                        assertThat(((BusinessException) exception).getErrorCode())
                                .isEqualTo(ErrorCode.INVALID_KEYWORD)
                );

        verifyNoInteractions(gatheringRepository);
    }

    @Test
    void 카테고리별_검색은_카테고리를_검증한_후_모임을_조회한다() {
        GatheringCategory category = mock(GatheringCategory.class);

        when(categoryRepository.findById(1))
                .thenReturn(Optional.of(category));
        when(gatheringRepository.findByCategoryId(
                eq(1),
                any(PageRequest.class)
        )).thenReturn(new PageImpl<>(
                List.of(createGathering()),
                PageRequest.of(0, 10),
                1
        ));

        var result = gatheringService.searchByCategory(1, 0, 10);

        assertThat(result.content()).hasSize(1);
        verify(categoryRepository).findById(1);
        verify(gatheringRepository).findByCategoryId(
                eq(1),
                any(PageRequest.class)
        );
    }

    @Test
    void 존재하지_않는_카테고리로_검색하면_예외가_발생한다() {
        when(categoryRepository.findById(999))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> gatheringService.searchByCategory(999, 0, 10))
                .isInstanceOf(BusinessException.class)
                .satisfies(exception ->
                        assertThat(((BusinessException) exception).getErrorCode())
                                .isEqualTo(ErrorCode.CATEGORY_NOT_FOUND)
                );

        verify(gatheringRepository, never())
                .findByCategoryId(anyInt(), any(PageRequest.class));
    }

    @Test
    void 모임을_생성하면_관리자_등록과_채팅방_생성을_함께_수행한다() {
        CreateGatheringRequest request = CreateGatheringRequest.builder()
                .gatheringName("백엔드 스터디")
                .description("Spring 스터디")
                .categoryId(1)
                .build();
        GatheringCategory category = mock(GatheringCategory.class);
        User manager = createUser();
        Gathering savedGathering = createGathering();

        when(gatheringRepository.existsByGatheringName(request.gatheringName()))
                .thenReturn(false);
        when(categoryRepository.findById(1))
                .thenReturn(Optional.of(category));
        when(userRepository.findById(10))
                .thenReturn(Optional.of(manager));
        when(gatheringRepository.save(any(Gathering.class)))
                .thenReturn(savedGathering);

        var result = gatheringService.create(10, request);

        assertThat(result.gatheringId()).isEqualTo(1);
        assertThat(result.gatheringName()).isEqualTo("백엔드 스터디");
        verify(memberRepository).save(any());
        verify(chatRoomRepository).save(any(ChatRoom.class));
    }

    @Test
    void 모임_수정은_관리자만_가능하다() {
        Gathering gathering = createGathering();
        UpdateGatheringRequest request = UpdateGatheringRequest.builder()
                .gatheringName("수정된 스터디")
                .description("수정된 설명")
                .build();

        when(gatheringRepository.findDetailById(1))
                .thenReturn(Optional.of(gathering));
        when(gatheringRepository.existsByGatheringNameAndIdNot(
                request.gatheringName(), 1
        )).thenReturn(false);

        var result = gatheringService.update(10, 1, request);

        assertThat(result.gatheringName()).isEqualTo("수정된 스터디");
        assertThat(result.description()).isEqualTo("수정된 설명");
    }

    @Test
    void 관리자가_아닌_사용자가_모임을_수정하면_예외가_발생한다() {
        Gathering gathering = createGathering();
        UpdateGatheringRequest request = UpdateGatheringRequest.builder()
                .gatheringName("수정된 스터디")
                .description("수정된 설명")
                .build();

        when(gatheringRepository.findDetailById(1))
                .thenReturn(Optional.of(gathering));

        assertThatThrownBy(() -> gatheringService.update(99, 1, request))
                .isInstanceOf(BusinessException.class)
                .satisfies(exception ->
                        assertThat(((BusinessException) exception).getErrorCode())
                                .isEqualTo(ErrorCode.GATHERING_MANAGER_ONLY)
                );

        verify(gatheringRepository, never())
                .existsByGatheringNameAndIdNot(anyString(), anyInt());
    }

    @Test
    void 모임_삭제는_관리자만_가능하다() {
        Gathering gathering = createGathering();

        when(gatheringRepository.findDetailById(1))
                .thenReturn(Optional.of(gathering));

        gatheringService.delete(10, 1);

        assertThat(gathering.isDeleted()).isTrue();
    }

    private Gathering createGathering() {
        return Gathering.builder()
                .id(1)
                .gatheringName("백엔드 스터디")
                .description("Spring 스터디")
                .category(mockCategory("개발"))
                .manager(createUser())
                .build();
    }

    private GatheringCategory mockCategory(String label) {
        GatheringCategory category = mock(GatheringCategory.class);
        when(category.getLabel()).thenReturn(label);
        return category;
    }

    private User createUser() {
        return User.builder()
                .id(10)
                .username("관리자")
                .email("manager@test.com")
                .password("encoded-password")
                .status(UserStatus.ACTIVE)
                .build();
    }
}
