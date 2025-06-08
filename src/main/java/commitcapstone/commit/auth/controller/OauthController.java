package commitcapstone.commit.auth.controller;

import commitcapstone.commit.auth.dto.response.AccessTokenResponse;
import commitcapstone.commit.auth.dto.request.RefreshTokenRequest;
import commitcapstone.commit.auth.dto.oauth.TokenRequest;
import commitcapstone.commit.auth.dto.response.LoginResponse;
import commitcapstone.commit.auth.service.OauthService;
import commitcapstone.commit.auth.service.RedisService;
import commitcapstone.commit.common.exception.OauthException;
import commitcapstone.commit.common.response.SuccessResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;
    private final RedisService redisService;


    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginResponse>> login(@RequestBody TokenRequest tokenRequest) {
        LoginResponse dto = oauthService.login(tokenRequest);

        return ResponseEntity.ok(new SuccessResponse<>("로그인 성공" ,dto));
    }


    @PostMapping("/logout")
    public ResponseEntity<SuccessResponse<?>> logout(@RequestBody RefreshTokenRequest request) {
        oauthService.logout(request);

        return ResponseEntity.ok(new SuccessResponse<>("로그아웃 성공", null));
    }

    @PostMapping("/refresh")
    public ResponseEntity<SuccessResponse<AccessTokenResponse>> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        String accessToken = oauthService.refresh(request);
        AccessTokenResponse dto = new AccessTokenResponse("Bearer", accessToken);

        return ResponseEntity.ok(new SuccessResponse<>("토큰 재발급 성공", dto));
    }


}
