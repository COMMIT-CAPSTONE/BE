package commitcapstone.commit.community.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityPostRequest {
    private String title;
    private String comntent;
}
