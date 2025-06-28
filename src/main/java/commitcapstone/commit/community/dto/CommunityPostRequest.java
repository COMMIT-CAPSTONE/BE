package commitcapstone.commit.community.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "커뮤니티 게시글 작성 요청 DTO")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPostRequest {
    @Schema(description = "게시글 제목", example = "첫 번째 게시글입니다.")
    private String title;

    @Schema(description = "게시글 내용", example = "이것은 게시글의 본문입니다.")
    private String content;
}
