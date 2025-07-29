package commitcapstone.commit.community.dto;


import commitcapstone.commit.tier.TierType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommunityPostResponse {
    private Long id; // 게시글 ID
    private String title; // 게시글 제목
    private String content; // 게시글 내용
    private String authorName; // 작성자 이름
    private Long authorId; // 작성자 ID
    private TierType authorTier; // 작성자 티어
    private LocalDateTime createdAt; // 작성일시
}

