package io.github.crewhub.service.gathering;

import io.github.crewhub.dto.gathering.response.GatheringCategoryResponse;
import io.github.crewhub.entity.gathering.GatheringCategory;
import io.github.crewhub.repository.gathering.GatheringCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * 모임 카테고리 캐시 관리 서비스
 */
@Service
@RequiredArgsConstructor
public class GatheringCategoryCacheService {
    private static final Logger log = LoggerFactory.getLogger(GatheringCategoryCacheService.class);

    static final String CACHE_KEY = "gathering:categories";
    private static final Duration CACHE_TTL = Duration.ofHours(1);

    private final GatheringCategoryRepository categoryRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public List<GatheringCategoryResponse> getCategories() {
        List<GatheringCategoryResponse> cachedCategories = getCachedCategories();

        if (cachedCategories != null) {
            return cachedCategories;
        }

        List<GatheringCategoryResponse> categories = categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        cacheCategories(categories);

        return categories;
    }

    private List<GatheringCategoryResponse> getCachedCategories() {
        try {
            Object cachedValue = redisTemplate.opsForValue().get(CACHE_KEY);

            if (cachedValue instanceof List<?> cachedCategories
                    && cachedCategories.stream().allMatch(GatheringCategoryResponse.class::isInstance)) {
                @SuppressWarnings("unchecked")
                List<GatheringCategoryResponse> result = (List<GatheringCategoryResponse>) cachedCategories;

                return result;
            }
        } catch (RuntimeException e) {
            log.warn("Failed to read gathering category cache: key={}", CACHE_KEY, e);
        }

        return null;
    }

    private void cacheCategories(List<GatheringCategoryResponse> categories) {
        try {
            redisTemplate.opsForValue().set(CACHE_KEY, categories, CACHE_TTL);
        } catch (RuntimeException e) {
            log.warn("Failed to write gathering category cache: key={}", CACHE_KEY, e);
        }
    }

    private GatheringCategoryResponse toResponse(GatheringCategory category) {
        return GatheringCategoryResponse.builder()
                .categoryId(category.getId())
                .key(category.getKey())
                .label(category.getLabel())
                .build();
    }
}
