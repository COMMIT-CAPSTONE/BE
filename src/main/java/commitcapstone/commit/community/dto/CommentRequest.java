package commitcapstone.commit.community.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class CommentRequest {
    @Schema(description = "부모 댓글 ID (대댓글이 아닌 경우 null)", example = "123")
    private Long parentCommentId;

    @Schema(description = "댓글 내용", example = "이것은 댓글입니다.")
    private String content;
}