package commitcapstone.commit.oauth.kakao.controller;

import commitcapstone.commit.oauth.google.dto.googleOauthAccessToken;
import commitcapstone.commit.oauth.google.service.googleService;
import commitcapstone.commit.oauth.kakao.dto.kakaoOauthAccessToken;
import commitcapstone.commit.oauth.kakao.dto.kakaoToken;
import commitcapstone.commit.oauth.kakao.service.kakaoService;
import jakarta.servlet.http.HttpSession;
import org.apache.coyote.Response;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class kakaoOauthController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(kakaoOauthController.class);
    private final kakaoService oauthService;

    public kakaoOauthController(kakaoService oauthService) {
        this.oauthService = oauthService;
    }


    @GetMapping("/oauth/kakao/login")
    public ResponseEntity<String> login() {
        String OauthURL = oauthService.createOauthURL();

        return ResponseEntity.ok(OauthURL);
    }

    @GetMapping("/oauth/kakao/redirect")
    public ResponseEntity<kakaoToken> redirect(@RequestParam String code, HttpSession session) {
        kakaoToken token = oauthService.getkakaoToken(code);
        final String refreshToken = token.getRefreshToken();
        session.setAttribute("refreshToken", token.getRefreshToken());
        LOGGER.info("저장한 리프레시토큰 : " + refreshToken);
        LOGGER.info("세션 아이디 : " + session.getId());
        return ResponseEntity.ok(token);
    }

    @PostMapping("oauth/kakao/token")
    public kakaoOauthAccessToken refresh(HttpSession session) {
        String refreshToken = (String) session.getAttribute("refreshToken");
        LOGGER.info("꺼낸 리프레시토큰 : " + refreshToken);
        LOGGER.info("세션 아이디 : " + session.getId());
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("세션에 저장된 refreshToken 비어있음");
        }
        return oauthService.getkakaoAccesstoken(refreshToken);
    }
}
