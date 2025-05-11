package commitcapstone.commit.oauth.provider.naver.controller;

import commitcapstone.commit.oauth.OauthController;
import commitcapstone.commit.oauth.accessTokenRequest;
import commitcapstone.commit.oauth.provider.naver.dto.naverOauthAccessToken;
import commitcapstone.commit.oauth.provider.naver.dto.naverToken;
import commitcapstone.commit.oauth.provider.naver.dto.naverValidToken;
import commitcapstone.commit.oauth.provider.naver.service.naverService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class naverOauthController implements OauthController<naverToken, naverOauthAccessToken, naverValidToken> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(naverOauthController.class);
    private final naverService oauthService;

    public naverOauthController(naverService oauthService) {
        this.oauthService = oauthService;
    }


    @GetMapping("/oauth/naver/login")
    public ResponseEntity<String> login(HttpSession session) {
        String OauthURL = oauthService.createOauthURL(session);

        return ResponseEntity.ok(OauthURL);
    }

    @GetMapping("/oauth/naver/redirect")
    public ResponseEntity<naverToken> redirect(@RequestParam String code, String state, HttpSession session) {
        String sessionState = (String) session.getAttribute("state");

        if (!sessionState.equals(state)) {
            throw new RuntimeException("state 인증 실패");
        }

        naverToken token = oauthService.getToken(session, state, code);
        final String refreshToken = token.getRefreshToken();

        session.setAttribute("refreshToken", token.getRefreshToken());
        LOGGER.info("저장한 리프레시토큰 : " + refreshToken);
        LOGGER.info("세션 아이디 : " + session.getId());
        return ResponseEntity.ok(token);
    }


    @PostMapping("oauth/naver/token")
    public ResponseEntity<naverOauthAccessToken> refresh(HttpSession session) {
        String refreshToken = (String) session.getAttribute("refreshToken");
        LOGGER.info("꺼낸 리프레시토큰 : " + refreshToken);
        LOGGER.info("세션 아이디 : " + session.getId());

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("세션에 저장된 refreshToken 비어있음");
        }

        naverOauthAccessToken token = oauthService.getReToken(refreshToken);
        return ResponseEntity.ok(token);
    }



    @PostMapping("oauth/naver/verify-accesstoken")
    public ResponseEntity<naverValidToken> verifyAccessToken(@RequestBody accessTokenRequest request) {
        LOGGER.info("accesstoken 받음?"  + request.getAccessToken());
        naverValidToken data = oauthService.verifyToken(request.getAccessToken());
        return ResponseEntity.ok(data);
    }

}

