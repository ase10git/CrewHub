package io.github.crewhub.service.gathering;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 모임 서비스
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class GatheringService {
}
