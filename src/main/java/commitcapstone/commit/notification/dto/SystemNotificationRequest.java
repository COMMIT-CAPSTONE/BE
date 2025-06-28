package commitcapstone.commit.notification.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "시스템 알림 요청 DTO")
public class SystemNotificationRequest {
    @Schema(description = "알림 제목", example = "시스템 점검 안내")
    private String title;

    @Schema(description = "알림 내용", example = "시스템 점검이 예정되어 있습니다. 자세한 내용은 공지사항을 확인해주세요.")
    private String content;
}
