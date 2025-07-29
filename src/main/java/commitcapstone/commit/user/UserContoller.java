package commitcapstone.commit.user;

import commitcapstone.commit.auth.dto.oauth.naverInfo;
import commitcapstone.commit.common.response.SuccessResponse;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserContoller {
    private final UserService userService;

    @GetMapping("info")
    public ResponseEntity<SuccessResponse<UserInfoResponse>> getUserInfo(@AuthenticationPrincipal String email) {
        UserInfoResponse userInfo = userService.getUserInfo(email);
        return ResponseEntity.ok(new SuccessResponse<>("유저 정보 조회 성공", userInfo));
    }

    @PostMapping("/name")
    public ResponseEntity<SuccessResponse<?>> updateUserName(@AuthenticationPrincipal String email, @RequestParam String name) {
        userService.updateUserName(email, name);
        return ResponseEntity.ok(new SuccessResponse<>("유저 이름 변경 성공", null));
    }

    @PostMapping("/profile")
    public ResponseEntity<SuccessResponse<?>> updateUserProfile(@AuthenticationPrincipal String email, @RequestParam int newProfile) {
        userService.updateUserProfile(email, newProfile);
        return ResponseEntity.ok(new SuccessResponse<>("유저 프로필 변경 성공", null));
    }
}
