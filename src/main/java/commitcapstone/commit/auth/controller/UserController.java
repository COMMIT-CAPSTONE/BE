package commitcapstone.commit.auth.controller;

import commitcapstone.commit.auth.config.jwt.JwtTokenProvider;
import commitcapstone.commit.auth.dto.request.UserInfoRequest;
import commitcapstone.commit.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public UserController(JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @PostMapping("/info")
    public ResponseEntity<?> info(@RequestBody UserInfoRequest userInfo, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        String email = jwtTokenProvider.getUserEmail(token);
        userService.setUserInfo(userInfo, email);

        return ResponseEntity.ok("저장됨");
    }




}