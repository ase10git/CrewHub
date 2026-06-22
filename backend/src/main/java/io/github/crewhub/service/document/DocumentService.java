package io.github.crewhub.service.document;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 문서 서비스
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DocumentService {
}
