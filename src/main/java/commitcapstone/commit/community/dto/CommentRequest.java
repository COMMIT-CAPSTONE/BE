package commitcapstone.commit.community.dto;

import lombok.Getter;

@Getter
public class CommentRequest {
    private Long parentCommentId;
    private String content;
}
