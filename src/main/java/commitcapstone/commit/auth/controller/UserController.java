package commitcapstone.commit.auth.controller;

import commitcapstone.commit.auth.config.jwt.JwtTokenProvider;
import commitcapstone.commit.auth.dto.request.UserInfoRequest;
import commitcapstone.commit.auth.service.UserService;
import commitcapstone.commit.common.response.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/info")
    public ResponseEntity<SuccessResponse<?>> info(@RequestBody UserInfoRequest userInfo, @RequestHeader("Authorization") String authorizationHeader) {
        userService.setUserInfo(userInfo, authorizationHeader);

        return ResponseEntity.ok(new SuccessResponse<>("유저 정보 저장 성공",null));
    }




}