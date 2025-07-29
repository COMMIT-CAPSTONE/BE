package commitcapstone.commit.notification.controller;

import commitcapstone.commit.common.response.SuccessResponse;
import commitcapstone.commit.notification.dto.NotificationUnReadResponse;
import commitcapstone.commit.notification.dto.SystemNotificationRequest;
import commitcapstone.commit.notification.service.NotificationService;
import commitcapstone.commit.notification.dto.NotificationViewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;
    // 알림 조회 API (가장 오래된 알림 순으로 표시된다 (읽은 알림은 맨 아래로))
    @GetMapping("/views")
    public ResponseEntity<SuccessResponse<NotificationViewResponse>> getNotificationViews(@AuthenticationPrincipal String email) {
        NotificationViewResponse response = notificationService.getNotificationViews(email);
        return ResponseEntity.ok(new SuccessResponse<>("알림 조회 성공", response));
    }

    @GetMapping("/unread")
    public ResponseEntity<SuccessResponse<NotificationUnReadResponse>> getUnread(@AuthenticationPrincipal String email) {
        NotificationUnReadResponse response = notificationService.getUnread(email);
        return ResponseEntity.ok(new SuccessResponse<>("읽지 않은 알림 조회 성공", response));
    }

    @PostMapping("/system")
    public ResponseEntity<SuccessResponse<String>> sendSystemNotification(@RequestBody SystemNotificationRequest request) {

        notificationService.sendSystemNotification(request);
        return ResponseEntity.ok(new SuccessResponse<>("시스템 알림 전송 성공", "시스템 알림이 전송되었습니다."));
    }

}
