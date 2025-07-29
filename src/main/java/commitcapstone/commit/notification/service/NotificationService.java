package commitcapstone.commit.notification.service;

import commitcapstone.commit.common.code.UserErrorCode;
import commitcapstone.commit.common.exception.UserException;
import commitcapstone.commit.notification.dto.NotificationBase;
import commitcapstone.commit.notification.dto.NotificationUnReadResponse;
import commitcapstone.commit.notification.dto.SystemNotificationRequest;
import commitcapstone.commit.user.User;
import commitcapstone.commit.user.UserRepository;
import commitcapstone.commit.notification.NotificationRepository;
import commitcapstone.commit.notification.entity.Notification;
import commitcapstone.commit.notification.entity.NotificationType;
import commitcapstone.commit.notification.dto.NotificationViewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<NotificationBase> list = new java.util.ArrayList<>();
        for (Notification notification : notifications) {
            list.add(NotificationBase.builder()
                    .title(notification.getTitle())
                    .conetnt(notification.getContent())
                    .date(notification.getCreatedAt())
                    .type(notification.getType())
                    .build());
        }
        return new NotificationViewResponse(list);
    }

    public NotificationUnReadResponse getUnread(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        int unreadCount = notificationRepository.countByUserIdAndIsReadFalse(user.getId());
        return new NotificationUnReadResponse(unreadCount);
    }

    public void sendSystemNotification(SystemNotificationRequest request) {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            Notification notification = Notification.builder()
                    .user(user)
                    .title(request.getTitle())
                    .content(request.getContent())
                    .type(NotificationType.SYSTEM)
                    .isRead(false)
                    .build();
            notificationRepository.save(notification);
        }
    }
}
