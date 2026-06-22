package io.github.crewhub.service.document;

import io.github.crewhub.common.exception.BusinessException;
import io.github.crewhub.dto.common.PageResponse;
import io.github.crewhub.dto.document.request.CreateDocumentRequest;
import io.github.crewhub.dto.document.response.CategoryResponse;
import io.github.crewhub.dto.document.response.CreateDocumentResponse;
import io.github.crewhub.dto.document.response.DocumentDetailResponse;
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

    public DocumentDetailResponse getDocument(Integer userId, Integer documentId) {
        Document document =
                documentRepository.findDetailById(documentId)
                        .orElseThrow(
                                () -> new BusinessException(ErrorCode.DOCUMENT_NOT_FOUND)
                        );

        validateMember(userId, document.getGathering().getId());

        List<CategoryResponse> categoryResponseList =
                categoryMapRepository.findDocumentCategory(documentId)
                        .stream()
                        .map(categoryMap ->
                                CategoryResponse.builder()
                                        .categoryId(categoryMap.getCategory().getId())
                                        .key(categoryMap.getCategory().getKey())
                                        .label(categoryMap.getCategory().getLabel())
                                        .build())
                        .toList();

        return DocumentDetailResponse.builder()
                .documentId(documentId)
                .categoryList(categoryResponseList)
                .writerId(document.getWriter().getId())
                .writerName(document.getWriter().getUsername())
                .gatheringId(document.getGathering().getId())
                .gatheringName(document.getGathering().getGatheringName())
                .title(document.getTitle())
                .content(document.getContent())
                .views(document.getViews())
                .isDeleted(document.getIsDeleted())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public PageResponse<DocumentSummaryResponse> searchByTitle(
            Integer userId,
            Integer gatheringId,
            String keyword,
            int page,
            int size
    ) {
        Gathering gathering = findGathering(gatheringId);

        validateMember(userId, gathering.getId());

        Pageable pageable = PageRequest.of(page, size);

        Page<Document> documents = documentRepository.findByTitle(gatheringId, keyword, pageable);

        return new PageResponse<>(
                documents.stream()
                        .map(this::toSummaryResponse)
                        .toList(),
                documents.getNumber(),
                documents.getSize(),
                documents.getTotalElements(),
                documents.getTotalPages(),
                documents.hasNext()
        );
    }

    public PageResponse<DocumentSummaryResponse> searchByCategory(
            Integer userId,
            Integer gatheringId,
            Integer categoryId,
            int page,
            int size
    ) {
        Gathering gathering = findGathering(gatheringId);

        validateMember(userId, gathering.getId());

        Pageable pageable = PageRequest.of(page, size);

        getCategory(categoryId);

        Page<DocumentCategoryMap> documentCategoryMaps =
                categoryMapRepository.findDocumentByCategory(
                        gatheringId,
                        categoryId,
                        pageable
                );

        return new PageResponse<>(
                documentCategoryMaps.stream()
                        .map(this::toSummaryResponse)
                        .toList(),
                documentCategoryMaps.getNumber(),
                documentCategoryMaps.getSize(),
                documentCategoryMaps.getTotalElements(),
                documentCategoryMaps.getTotalPages(),
                documentCategoryMaps.hasNext()
        );
    }

    private DocumentSummaryResponse toSummaryResponse(Document document) {
        return DocumentSummaryResponse.builder()
                .documentId(document.getId())
                .title(document.getTitle())
                .writerId(document.getWriter().getId())
                .writerName(document.getWriter().getUsername())
                .views(document.getViews())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    private DocumentSummaryResponse toSummaryResponse(DocumentCategoryMap documentCategoryMaps) {
        return DocumentSummaryResponse.builder()
                .documentId(documentCategoryMaps.getDocument().getId())
                .title(documentCategoryMaps.getDocument().getTitle())
                .writerId(documentCategoryMaps.getDocument().getWriter().getId())
                .writerName(documentCategoryMaps.getDocument().getWriter().getUsername())
                .views(documentCategoryMaps.getDocument().getViews())
                .createdAt(documentCategoryMaps.getDocument().getCreatedAt())
                .updatedAt(documentCategoryMaps.getDocument().getUpdatedAt())
                .build();
    }

    private DocumentCategory getCategory(Integer categoryId) {
        return categoryRepository
                .findById(categoryId).orElseThrow(
                        () -> new BusinessException(ErrorCode.CATEGORY_NOT_FOUND)
                );
    }
}
