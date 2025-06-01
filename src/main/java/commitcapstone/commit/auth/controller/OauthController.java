package commitcapstone.commit.auth.controller;

import commitcapstone.commit.auth.dto.response.AccessTokenResponse;
import commitcapstone.commit.auth.dto.request.RefreshTokenRequest;
import commitcapstone.commit.auth.dto.oauth.TokenRequest;
import commitcapstone.commit.auth.dto.response.LoginResponse;
import commitcapstone.commit.auth.service.OauthService;
import commitcapstone.commit.auth.service.RedisService;
import commitcapstone.commit.common.exception.OauthException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class OauthController {

    private final OauthService oauthService;
    private final RedisService redisService;

    public OauthController(OauthService oauthService, RedisService redisService) {
        this.oauthService = oauthService;
        this.redisService = redisService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody TokenRequest tokenRequest) {
        return ResponseEntity.ok(oauthService.login(tokenRequest));
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {
        oauthService.logout(request);
        return ResponseEntity.status(200).body("success logout");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        String accessToken = oauthService.refresh(request);
        return ResponseEntity.ok(new AccessTokenResponse("Bearer", accessToken));
    }


}
