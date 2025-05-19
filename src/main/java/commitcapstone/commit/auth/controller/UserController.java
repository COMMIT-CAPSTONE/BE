package commitcapstone.commit.auth.controller;

import commitcapstone.commit.auth.config.jwt.JwtTokenProvider;
import commitcapstone.commit.auth.dto.AccessTokenResponse;
import commitcapstone.commit.auth.dto.RefreshTokenRequest;
import commitcapstone.commit.auth.dto.UserInfo;
import commitcapstone.commit.auth.service.RedisService;
import commitcapstone.commit.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    public UserController(JwtTokenProvider jwtTokenProvider, RedisService redisService, UserService userService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisService = redisService;
        this.userService = userService;
    }

    @PostMapping("/info")
    public ResponseEntity<?> info(@RequestBody UserInfo userInfo, @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");

        String email = jwtTokenProvider.getUserEmail(token);
        userService.setUserInfo(userInfo, email);

        return ResponseEntity.ok("저장됨");
    }



}