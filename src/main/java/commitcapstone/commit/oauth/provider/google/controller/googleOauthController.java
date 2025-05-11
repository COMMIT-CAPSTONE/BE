package commitcapstone.commit.oauth.provider.google.controller;

import commitcapstone.commit.oauth.OauthController;
import commitcapstone.commit.oauth.accessTokenRequest;
import commitcapstone.commit.oauth.provider.google.dto.googleOauthAccessToken;
import commitcapstone.commit.oauth.provider.google.dto.googleToken;
import commitcapstone.commit.oauth.provider.google.dto.googleValidToken;
import commitcapstone.commit.oauth.provider.google.service.googleService;
import commitcapstone.commit.oauth.provider.naver.dto.naverValidToken;
import commitcapstone.commit.oauth.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class googleOauthController implements OauthController<googleToken, googleOauthAccessToken, googleValidToken> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(googleOauthController.class);
    private final googleService oauthService;
    private final UserRepository userRepository;

    @Autowired
    public googleOauthController(googleService oauthService, UserRepository userRepository) {
        this.oauthService = oauthService;
        this.userRepository = userRepository;
    }

    @GetMapping("oauth/google/login")
    public ResponseEntity<String> login(HttpSession session) {
        String OauthURL = oauthService.createOauthURL(session);

        return ResponseEntity.ok(OauthURL);
    }


    @GetMapping("oauth/google/redirect")
    public ResponseEntity<googleToken> redirect(@RequestParam String code, String state, HttpSession session) {

        String sessionState = (String) session.getAttribute("state");

        if (!sessionState.equals(state)) {
            throw new RuntimeException("state 인증 실패");
        }

        googleToken token = oauthService.getToken(session, state, code);

        final String refreshToken = token.getRefreshToken();
        session.setAttribute("refresh_token", refreshToken);

        LOGGER.info("저장한 리프레시토큰 : " + refreshToken);
        LOGGER.info("세션 아이디 : " + session.getId());


        return ResponseEntity.ok(token);
    }


    @PostMapping("oauth/google/token")
    public ResponseEntity<googleOauthAccessToken> refresh(HttpSession session) {
        String refreshToken = (String) session.getAttribute("refreshToken");
        LOGGER.info("꺼낸 리프레시토큰 : " + refreshToken);
        LOGGER.info("세션 아이디 : " + session.getId());
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("세션에 저장된 refreshToken 비어있음");
        }

        googleOauthAccessToken token = oauthService.getReToken(refreshToken);
        return ResponseEntity.ok(token);
    }

    @PostMapping("oauth/google/verify-accesstoken")
    public ResponseEntity<googleValidToken> verifyAccessToken(@RequestBody accessTokenRequest request) {


        googleValidToken data = oauthService.verifyToken(request.getAccessToken());
        return ResponseEntity.ok(data);
    }
}
