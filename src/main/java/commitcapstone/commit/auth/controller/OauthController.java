package commitcapstone.commit.auth.controller;

import commitcapstone.commit.auth.dto.AccessTokenResponse;
import commitcapstone.commit.auth.dto.RefreshTokenRequest;
import commitcapstone.commit.auth.dto.UserInfo;
import commitcapstone.commit.auth.dto.oauth.OauthUserInfo;
import commitcapstone.commit.auth.dto.oauth.TokenRequest;
import commitcapstone.commit.auth.dto.oauth.OauthProvider;
import commitcapstone.commit.auth.service.OauthService;
import commitcapstone.commit.auth.config.jwt.JwtTokenProvider;
import commitcapstone.commit.auth.provider.naver.controller.naverOauthController;
import commitcapstone.commit.auth.service.RedisService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OauthController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(naverOauthController.class);
    private final OauthService oauthService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    public OauthController(OauthService oauthService, JwtTokenProvider jwtTokenProvider, RedisService redisService) {
        this.oauthService = oauthService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisService = redisService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody TokenRequest tokenRequest, HttpSession session) {
        String provider = tokenRequest.getProvider();
        String oauthAccessToken = tokenRequest.getAccessToken();

        if (provider == null) {
            throw new RuntimeException("소셜 로그인 제공자 null");
        }

        if (oauthAccessToken == null) {
            throw new RuntimeException("Oauth accessToken null");
        }

        OauthUserInfo userInfo = oauthService.getUserInfo(
                OauthProvider.valueOf(provider.toUpperCase()), oauthAccessToken
        );

        String userId = userInfo.getId();
        String userEmail = userInfo.getEmail();

        LOGGER.info("제공자 : {},  유저 ID: {}, 이메일: {}", provider, userId, userEmail);

        String accessToken = jwtTokenProvider.createAccessToken(userEmail);
        String refreshToken = jwtTokenProvider.createRefreshToken(userEmail);

        String status = "";
        if (oauthService.isUserCheck(provider , userEmail)) {
            status = "not_fitst_login";
        } else {
            status = "fitst_login";
        }

        redisService.save("refreshToken:" + userEmail, refreshToken, 604800000);
        redisService.save("provider:" + userEmail, provider, 1800000 ); // 30분 저장
        redisService.save("oauthId:" + userEmail, userId, 1800000 ); // 30분 저장
        return ResponseEntity.ok(Map.of("token_type", "Bearer", "access_token" , accessToken, "refresh_token", refreshToken, "status" , status));
    }















    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body("logout/ Invalid Refresh Token");
        }
        String email = jwtTokenProvider.getUserEmail(refreshToken);
        String storedRefreshToken = redisService.get("refreshToken:" + email);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            return ResponseEntity.status(401).body("Refresh Token not found or mismatched");
        }

        redisService.deleteRefreshToken("refreshToken:" + email);
        return ResponseEntity.status(200).body("success logout?");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(401).body("refresh/ Invalid Refresh Token");
        }

        String email = jwtTokenProvider.getUserEmail(refreshToken);
        String storedRefreshToken = redisService.get("refreshToken:"+email);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            return ResponseEntity.status(401).body("Refresh Token not found or mismatched");
        }
        String newAccessToken = jwtTokenProvider.createAccessToken(email);
        return ResponseEntity.ok(new AccessTokenResponse(newAccessToken, "Bearer"));
    }


}
