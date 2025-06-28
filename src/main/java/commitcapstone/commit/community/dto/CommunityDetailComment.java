package commitcapstone.commit.community.dto;

import commitcapstone.commit.tier.TierType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityDetailComment {
    private Long commentId;
    private String authorName;
    private Long authorId;
    private TierType authorTier;
    private String content;
    private LocalDateTime createdAt;
    private List<CommunityDetailComment> replies; // 대댓글
}
