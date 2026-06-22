package io.github.crewhub.entity.document;

import io.github.crewhub.entity.common.BaseEntity;
import io.github.crewhub.entity.gathering.Gathering;
import io.github.crewhub.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

/**
 * 문서 Entity
 */
@Entity
@Table(name = "documents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Document extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gathering_id", nullable = false)
    private Gathering gathering;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(
            name = "content",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String content;

    @Builder.Default
    @Column(name = "views", nullable = false)
    private Integer views = 0;

    @Builder.Default
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    public void updateDocument(
            String title,
            String content
    ) {
        this.title = title;
        this.content = content;
    }
}