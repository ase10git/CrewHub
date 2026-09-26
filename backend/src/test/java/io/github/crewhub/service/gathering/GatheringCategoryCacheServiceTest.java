package io.github.crewhub.service.gathering;

import io.github.crewhub.dto.gathering.response.GatheringCategoryResponse;
import io.github.crewhub.entity.gathering.GatheringCategory;
import io.github.crewhub.repository.gathering.GatheringCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GatheringCategoryCacheServiceTest {
    private static final String CACHE_KEY = "gathering:categories";

    @Mock
    private GatheringCategoryRepository categoryRepository;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    private GatheringCategoryCacheService cacheService;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        cacheService = new GatheringCategoryCacheService(categoryRepository, redisTemplate);
    }

    @Test
    void 캐시가_존재하면_DB를_조회하지_않는다() {
        List<GatheringCategoryResponse> cachedCategories = List.of(
                GatheringCategoryResponse.builder()
                        .categoryId(1)
                        .key("study")
                        .label("스터디")
                        .build()
        );
        when(valueOperations.get(CACHE_KEY)).thenReturn(cachedCategories);

        List<GatheringCategoryResponse> result = cacheService.getCategories();

        assertEquals(cachedCategories, result);
        verify(categoryRepository, never()).findAll();
    }

    @Test
    void 캐시가_없으면_DB를_조회하고_캐시에_저장한다() {
        GatheringCategory category = mock(GatheringCategory.class);
        when(category.getId()).thenReturn(1);
        when(category.getKey()).thenReturn("study");
        when(category.getLabel()).thenReturn("스터디");

        when(valueOperations.get(CACHE_KEY)).thenReturn(null);
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<GatheringCategoryResponse> result = cacheService.getCategories();

        assertEquals(
                List.of(
                        GatheringCategoryResponse.builder()
                                .categoryId(1)
                                .key("study")
                                .label("스터디")
                                .build()
                ),
                result
        );
        verify(categoryRepository).findAll();
        verify(valueOperations).set(eq(CACHE_KEY), eq(result), any(Duration.class));
    }

    @Test
    void Redis_조회가_실패해도_DB_조회로_기존_동작을_유지한다() {
        GatheringCategory category = mock(GatheringCategory.class);
        when(category.getId()).thenReturn(1);
        when(category.getKey()).thenReturn("study");
        when(category.getLabel()).thenReturn("스터디");

        when(valueOperations.get(CACHE_KEY)).thenThrow(new RuntimeException("Redis unavailable"));
        when(categoryRepository.findAll()).thenReturn(List.of(category));
        doThrow(new RuntimeException("Redis unavailable"))
                .when(valueOperations).set(eq(CACHE_KEY), any(), any(Duration.class));

        List<GatheringCategoryResponse> result = cacheService.getCategories();

        assertEquals(1, result.size());
        assertEquals("study", result.get(0).key());
        verify(categoryRepository).findAll();
    }
}
