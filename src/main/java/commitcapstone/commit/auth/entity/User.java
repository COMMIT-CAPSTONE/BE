package commitcapstone.commit.auth.entity;

import commitcapstone.commit.tier.TierType;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "tier")
    private TierType tier = TierType.헬린이;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Gym gym;
}