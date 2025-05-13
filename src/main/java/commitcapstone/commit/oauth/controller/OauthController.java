package commitcapstone.commit.oauth.controller;

import commitcapstone.commit.oauth.dto.OauthUserInfo;
import commitcapstone.commit.oauth.dto.TokenRequest;
import commitcapstone.commit.oauth.dto.OauthProvider;
import commitcapstone.commit.oauth.service.OauthService;
import commitcapstone.commit.oauth.config.jwt.JwtTokenProvider;
import commitcapstone.commit.oauth.provider.naver.controller.naverOauthController;
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

    public OauthController(OauthService oauthService, JwtTokenProvider jwtTokenProvider) {
        this.oauthService = oauthService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody TokenRequest tokenRequest, HttpSession session) {
        String provider = tokenRequest.getProvider();
        String oauthAccessToken = tokenRequest.getAccessToken();

        LOGGER.info(String.valueOf(tokenRequest));
        LOGGER.info("provider: " + provider);
        LOGGER.info("accessToken: " + oauthAccessToken);
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

        session.setAttribute("refreshToken", refreshToken);
        return ResponseEntity.ok(Map.of("access_token" , accessToken));
    }

}
