package commitcapstone.commit.notification.service;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.auth.repository.UserRepository;
import commitcapstone.commit.challenge.entity.Challenge;
import commitcapstone.commit.notification.NotificationRepository;
import commitcapstone.commit.notification.entity.Notification;
import commitcapstone.commit.notification.entity.NotificationType;
import commitcapstone.commit.notification.dto.NotificationViewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;


    // 알림 관련 서비스 로직을 여기에 구현합니다.

    public void sendChallengeResultNotification(User user, NotificationType notificationType, String title, String content) {
        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .content(content)
                .type(notificationType)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
    }

    public NotificationViewResponse getNotificationViews(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));


        // 읽지 않은 알림 먼저(createdAt 오름차순), 그 다음 읽은 알림(createdAt 오름차순)
        List<Notification> notifications = notificationRepository.findByUserIdOrderByIsReadAscCreatedAtAsc(user.getId());
        return new NotificationViewResponse(notifications);
    }

}
