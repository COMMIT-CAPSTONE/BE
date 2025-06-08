package commitcapstone.commit.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth_provider", nullable = false, length = 50)
    private String oauthProvider;

    @Column(name = "oauth_id", nullable = false, length = 100, unique = true)
    private String oauthId;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(length = 50, unique = true)
    private String name;

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer exp;

    @Column(columnDefinition = "INT DEFAULT 0")
    private Integer point;

    @Column(length = 30, columnDefinition = "VARCHAR(30) DEFAULT 'bronze'")
    private String tier;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdA ;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Gym gym;
}