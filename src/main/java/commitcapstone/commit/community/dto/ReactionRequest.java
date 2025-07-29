package commitcapstone.commit.community.dto;

import commitcapstone.commit.community.entity.ReactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "리액션 요청 DTO")
@Getter
public class ReactionRequest {
    @Schema(description = "리액션 타입", example = "LIKE")
    private ReactionType reactionType;
}
