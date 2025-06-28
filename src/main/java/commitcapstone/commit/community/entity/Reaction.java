package commitcapstone.commit.community.entity;

import commitcapstone.commit.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reaction", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"target_id", "user_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 리액션 대상: 지금은 post만 지원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_id", nullable = false)
    private Community targetPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType reactionType;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public Reaction(Community targetPost, User user, ReactionType reactionType) {
        this.targetPost = targetPost;
        this.user = user;
        this.reactionType = reactionType;
    }
}
