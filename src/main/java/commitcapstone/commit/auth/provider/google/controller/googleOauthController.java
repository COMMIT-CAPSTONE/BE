package commitcapstone.commit.auth.provider.google.controller;


import commitcapstone.commit.auth.config.jwt.JwtTokenProvider;
import commitcapstone.commit.auth.provider.google.dto.googleToken;
import commitcapstone.commit.auth.provider.google.service.googleService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/oauth/google")
public class googleOauthController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(googleOauthController.class);
    private final googleService oauthService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public googleOauthController(googleService oauthService, JwtTokenProvider jwtTokenProvider) {
        this.oauthService = oauthService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> login(HttpSession session) {
        String state = UUID.randomUUID().toString();
        session.setAttribute("state", state);

        String OauthURL = oauthService.createOauthURL(state);

        return ResponseEntity.ok(Map.of("oauth_url", OauthURL));
    }


    @GetMapping("/redirect")
    public ResponseEntity<Map<String, String>> redirect(@RequestParam String code, String state, HttpSession session) {
        String sessionState = (String) session.getAttribute("state");
        LOGGER.info("sessionstate: " + sessionState);
        LOGGER.info("state" + state);
        if (sessionState == null || !sessionState.equals(state)) {
            throw new IllegalStateException("Invalid google Oauth state");
        }

        session.removeAttribute("state");
        // google에 토큰 요청
        googleToken token = oauthService.getToken(state, code);

        return ResponseEntity.ok(Map.of("oauth_access_token" , token.getAccessToken()));
    }


}
