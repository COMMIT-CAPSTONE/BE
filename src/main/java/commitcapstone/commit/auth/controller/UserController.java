package commitcapstone.commit.auth.controller;

import commitcapstone.commit.auth.config.jwt.JwtTokenProvider;
import commitcapstone.commit.auth.dto.AccessTokenResponse;
import commitcapstone.commit.auth.dto.RefreshTokenRequest;
import commitcapstone.commit.auth.dto.UserInfo;
import commitcapstone.commit.auth.service.RedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final RedisService redisService;
    JwtTokenProvider jwtTokenProvider;
    public UserController(JwtTokenProvider jwtTokenProvider, RedisService redisService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisService = redisService;
    }


    @GetMapping("/test")
    public ResponseEntity test() {
        return ResponseEntity.ok("됨 ㅇㅇ");
    }


    @PostMapping("/info")
    public void info(@RequestBody UserInfo userInfo) {

    }



}