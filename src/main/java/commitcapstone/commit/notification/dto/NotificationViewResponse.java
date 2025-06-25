package commitcapstone.commit.notification.dto;

import commitcapstone.commit.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@AllArgsConstructor
public class NotificationViewResponse {
    List<Notification> notifications;

}
