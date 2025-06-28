package commitcapstone.commit.community.dto;

import commitcapstone.commit.tier.TierType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommunityPostBase {
    private Long id; // 게시글 ID
    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private String authorName; // 작성자 이름
    private Long authorId; // 작성자 ID
    private int authorProfile; // 작성자 프로필 이미지
    private TierType authorTier; // 작성자 티어
    private int reactionCount; // 조회수
    private int commentCount; // 댓글 수
    private LocalDateTime createdAt; // 작성일시
    private LocalDateTime updatedAt; // 수정일시
}
