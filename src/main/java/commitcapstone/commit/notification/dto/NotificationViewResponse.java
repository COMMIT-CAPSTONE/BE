package commitcapstone.commit.notification.dto;

import commitcapstone.commit.notification.entity.Notification;
import lombok.*;

import java.util.List;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationViewResponse {
    List<NotificationBase> notifications;
}
