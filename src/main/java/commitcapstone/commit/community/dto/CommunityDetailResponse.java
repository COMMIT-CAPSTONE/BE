package commitcapstone.commit.community.dto;

import commitcapstone.commit.community.entity.ReactionType;
import commitcapstone.commit.tier.TierType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommunityDetailResponse {
    private Long id; // 게시글 ID
    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private String authorName; // 작성자 이름
    private Long authorId; // 작성자 ID
    private int authorProfile; // 작성자 프로필 이미지
    private TierType authorTier; // 작성자 티어
    private LocalDateTime createdAt; // 작성일시
    private LocalDateTime updatedAt; // 수정일시
    private ReactionType myReaction; // 사용자의 반응


    private Map<ReactionType, Integer> reaction;
    private List<CommunityDetailComment> comments;
}