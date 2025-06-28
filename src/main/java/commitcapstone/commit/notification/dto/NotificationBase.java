package commitcapstone.commit.notification.dto;

import commitcapstone.commit.notification.entity.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class NotificationBase {
    @Builder.Default
    private String iconPath = "alarm_loudspeacker.png";

    private String title;

    private String conetnt;

    private LocalDateTime date;


    private NotificationType type;
}
