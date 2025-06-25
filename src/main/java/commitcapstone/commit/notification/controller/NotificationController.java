package commitcapstone.commit.notification.controller;

import commitcapstone.commit.common.response.SuccessResponse;
import commitcapstone.commit.notification.service.NotificationService;
import commitcapstone.commit.notification.dto.NotificationViewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
