package io.github.crewhub.service.document;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.common.PageResponse;
import io.github.crewhub.dto.document.request.CreateDocumentRequest;
import io.github.crewhub.dto.document.response.CreateDocumentResponse;
import io.github.crewhub.dto.document.response.DocumentSummaryResponse;
import io.github.crewhub.entity.document.Document;
import io.github.crewhub.entity.document.DocumentCategory;
import io.github.crewhub.entity.document.DocumentCategoryMap;
import io.github.crewhub.entity.document.DocumentCategoryMapId;
import io.github.crewhub.entity.gathering.Gathering;
import io.github.crewhub.entity.user.User;
import io.github.crewhub.enums.common.ErrorCode;
import io.github.crewhub.repository.document.DocumentCategoryMapRepository;
import io.github.crewhub.repository.document.DocumentCategoryRepository;
import io.github.crewhub.repository.document.DocumentRepository;
import io.github.crewhub.repository.gathering.GatheringMemberRepository;
import io.github.crewhub.repository.gathering.GatheringRepository;
import io.github.crewhub.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 문서 서비스
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DocumentService {
    private final UserRepository userRepository;
    private final DocumentRepository documentRepository;
    private final DocumentCategoryRepository categoryRepository;
    private final DocumentCategoryMapRepository categoryMapRepository;
    private final GatheringRepository gatheringRepository;
    private final GatheringMemberRepository memberRepository;

    @Transactional
    public CreateDocumentResponse create(Integer userId, CreateDocumentRequest request) {
        User writer = getUser(userId);

        Gathering gathering = findGathering(request.gatheringId());

        validateMember(userId, gathering.getId());

        List<DocumentCategory> categories =
                categoryRepository.findAllById(request.categoryIds());

        if (categories.size() != request.categoryIds().size()) {
            throw new BusinessException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        Document document = Document.builder()
                        .writer(writer)
                        .gathering(gathering)
                        .title(request.title())
                        .content(request.content())
                        .build();

        Document saved = documentRepository.save(document);

        categories.forEach(category -> {
            DocumentCategoryMap map =
                    DocumentCategoryMap.builder()
                            .id(
                                    new DocumentCategoryMapId(
                                            saved.getId(),
                                            category.getId()
                                    )
                            )
                            .document(saved)
                            .category(category)
                            .build();

            categoryMapRepository.save(map);
        });

        return CreateDocumentResponse.builder()
                .documentId(saved.getId())
                .gatheringId(gathering.getId())
                .title(saved.getTitle())
                .categories(
                        categories.stream()
                                .map(DocumentCategory::getLabel)
                                .toList()
                )
                .createdAt(saved.getCreatedAt())
                .build();
    }

    private User getUser(Integer userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND)
        );
    }

    private Gathering findGathering(Integer gatheringId) {
        return gatheringRepository.findById(gatheringId)
                .orElseThrow(
                        () -> new BusinessException(ErrorCode.GATHERING_NOT_FOUND)
                );
    }

    private void validateMember(Integer userId, Integer gatheringId) {
        boolean exists = memberRepository.existsByGatheringIdAndUserId(gatheringId, userId);

        if (!exists) {
            throw new BusinessException(ErrorCode.GATHERING_MEMBER_ONLY);
        }
    }

    public PageResponse<DocumentSummaryResponse> getDocuments(
            Integer userId,
            Integer gatheringId,
            int page,
            int size
    ) {
        validateMember(userId, gatheringId);

        Pageable pageable = PageRequest.of(page, size);

        Page<Document> documents =
                documentRepository.findByGatheringIdAndIsDeletedFalse(
                        gatheringId,
                        pageable
                );

        return new PageResponse<>(
                documents.stream()
                        .map(document ->
                                DocumentSummaryResponse.builder()
                                        .documentId(document.getId())
                                        .title(document.getTitle())
                                        .writerId(document.getWriter().getId())
                                        .writerName(document.getWriter().getUsername())
                                        .views(document.getViews())
                                        .createdAt(document.getCreatedAt())
                                        .updatedAt(document.getUpdatedAt())
                                        .build()
                        )
                        .toList(),
                documents.getNumber(),
                documents.getSize(),
                documents.getTotalElements(),
                documents.getTotalPages(),
                documents.hasNext()
        );
    }
}
