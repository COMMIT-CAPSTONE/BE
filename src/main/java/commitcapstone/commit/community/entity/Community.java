package commitcapstone.commit.community.entity;

import commitcapstone.commit.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "community")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    private String title;

    private String content;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private boolean isDeleted = false;

    @Column(name = "reaction_count", nullable = false)
    @Builder.Default
    private int reactionCount = 0;
    @Column(name = "comment_count", nullable = false)
    @Builder.Default
    private int commentCount = 0;
}
