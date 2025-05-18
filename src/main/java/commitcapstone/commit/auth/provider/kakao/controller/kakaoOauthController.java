package commitcapstone.commit.auth.provider.kakao.controller;


import commitcapstone.commit.auth.config.jwt.JwtTokenProvider;
import commitcapstone.commit.auth.provider.kakao.dto.kakaoToken;
import commitcapstone.commit.auth.provider.kakao.service.kakaoService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/oauth/kakao")
public class kakaoOauthController {
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

        return ResponseEntity.ok(Map.of("oauth_access_token" , token.getAccessToken()));
    }




}
