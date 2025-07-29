package commitcapstone.commit.user;

import commitcapstone.commit.tier.TierType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "users")
@AllArgsConstructor
@Builder
@NoArgsConstructor
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

    @Column(name = "profile")
    private int profile;

    @Enumerated(EnumType.STRING)
    @Column(name = "tier")
    @Builder.Default
    private TierType tier = TierType.헬린이;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;


}