package commitcapstone.commit.oauth.provider.kakao.controller;

import commitcapstone.commit.oauth.OauthController;
import commitcapstone.commit.oauth.accessTokenRequest;
import commitcapstone.commit.oauth.provider.kakao.dto.kakaoOauthAccessToken;
import commitcapstone.commit.oauth.provider.kakao.dto.kakaoToken;
import commitcapstone.commit.oauth.provider.kakao.dto.kakaoValidToken;
import commitcapstone.commit.oauth.provider.kakao.service.kakaoService;
import commitcapstone.commit.oauth.provider.naver.dto.naverValidToken;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class kakaoOauthController implements OauthController<kakaoToken, kakaoOauthAccessToken, kakaoValidToken> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(kakaoOauthController.class);
    private final kakaoService oauthService;

    public kakaoOauthController(kakaoService oauthService) {
        this.oauthService = oauthService;
    }


    @GetMapping("/oauth/kakao/login")
    public ResponseEntity<String> login(HttpSession session) {
        String OauthURL = oauthService.createOauthURL(session);

        return ResponseEntity.ok(OauthURL);
    }


    @GetMapping("/oauth/kakao/redirect")
    public ResponseEntity<kakaoToken> redirect(@RequestParam String code, String state, HttpSession session) {
        kakaoToken token = oauthService.getToken(session, state, code);
        final String refreshToken = token.getRefreshToken();
        session.setAttribute("refreshToken", token.getRefreshToken());
        LOGGER.info("저장한 리프레시토큰 : " + refreshToken);
        LOGGER.info("세션 아이디 : " + session.getId());
        return ResponseEntity.ok(token);
    }

    @PostMapping("oauth/kakao/token")
    public ResponseEntity<kakaoOauthAccessToken> refresh(HttpSession session) {
        String refreshToken = (String) session.getAttribute("refreshToken");
        LOGGER.info("꺼낸 리프레시토큰 : " + refreshToken);
        LOGGER.info("세션 아이디 : " + session.getId());
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("세션에 저장된 refreshToken 비어있음");
        }

        kakaoOauthAccessToken token = oauthService.getReToken(refreshToken);
        return ResponseEntity.ok(token);
    }



    @PostMapping("oauth/kakao/verify-accesstoken")
    public ResponseEntity<kakaoValidToken> verifyAccessToken(@RequestBody accessTokenRequest request) {

        kakaoValidToken data = oauthService.verifyToken(request.getAccessToken());
        return ResponseEntity.ok(data);
    }
}
