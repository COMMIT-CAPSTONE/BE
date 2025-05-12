package commitcapstone.commit.oauth.provider.kakao.controller;


import commitcapstone.commit.oauth.OauthController;
import commitcapstone.commit.oauth.config.jwt.JwtTokenProvider;
import commitcapstone.commit.oauth.provider.kakao.dto.kakaoInfo;
import commitcapstone.commit.oauth.provider.kakao.dto.kakaoToken;
import commitcapstone.commit.oauth.provider.kakao.service.kakaoService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.view.RedirectView;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/oauth/kakao")
public class kakaoOauthController implements OauthController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(kakaoOauthController.class);
    private final kakaoService oauthService;
    private final JwtTokenProvider jwtTokenProvider;
    public kakaoOauthController(kakaoService oauthService, JwtTokenProvider jwtTokenProvider) {
        this.oauthService = oauthService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @GetMapping("/login")
    public  ResponseEntity<Map<String, String>> login(HttpSession session) {

        String state = UUID.randomUUID().toString();
        session.setAttribute("state", state);

        String OauthURL = oauthService.createOauthURL(state);
        return ResponseEntity.ok(Map.of("oauth_url", OauthURL));

    }


    @GetMapping("/redirect")
    public  ResponseEntity<Map<String, String>> redirect(@RequestParam String code, String state, HttpSession session) {
        String sessionState = (String) session.getAttribute("state");

        if (sessionState == null || !sessionState.equals(state)) {
            throw new IllegalStateException("Invalid kakaoOauth state");
        }
        // kakao에 토큰 요청
        kakaoToken token = oauthService.getToken(state, code);

        // kakao에 사용자 정보 요청 (email과 고유 ID)
        kakaoInfo userInfo = oauthService.getUserInfo(token.getAccessToken());

        // 자체 JWT 생성
        String email = userInfo.getKakaoAccount().getEmail();
        LOGGER.info("이메일 정보 획득: "  + email);

        String accessToken = jwtTokenProvider.createAccessToken(email);
        String refreshToken = jwtTokenProvider.createRefreshToken(email);

        String sessionId = UUID.randomUUID().toString();
        session.setAttribute(sessionId, accessToken);

        session.setAttribute("refreshToken", refreshToken);




        return ResponseEntity.ok(Map.of("session_id" , sessionId));
    }

    @Override
    public ResponseEntity<Map<String, String>> exchange(Map<String, String> body, HttpSession session) {
        String sessionId = body.get("session_id");

        if (sessionId == null) {
            throw new RuntimeException("kakao Oauth exchange session_id is NULL");
        }

        LOGGER.info("session 값 보자 ㅅㅂ : " +  sessionId);

        String accessToken = (String) session.getAttribute(sessionId);
        if (accessToken == null) {
            throw new RuntimeException("kakao Oauth exchange accessToken is NULL");
        }


//            //todo: 토큰 검증 구현 필요
//            if(jwtTokenProvider.validateToken(accessToken)) {
//
//            }

        session.removeAttribute("sessionCode");

        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }



}
