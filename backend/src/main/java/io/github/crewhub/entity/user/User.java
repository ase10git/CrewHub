package io.github.crewhub.entity.user;


import io.github.crewhub.entity.common.BaseEntity;
import io.github.crewhub.enums.user.UserStatus;
import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자 Entity
 */
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "profile_image", length = 255)
    private String profileImage;

    public void updateProfile(
            String username,
            String description,
            String profileImage
    ) {
        this.username = username;
        this.description = description;
        this.profileImage = profileImage;
    }
}