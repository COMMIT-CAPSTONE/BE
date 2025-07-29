package commitcapstone.commit.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class NotificationUnReadResponse {
    private int unreadCount;
}
