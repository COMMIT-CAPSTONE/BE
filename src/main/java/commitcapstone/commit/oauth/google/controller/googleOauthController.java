package commitcapstone.commit.oauth.google.controller;

import commitcapstone.commit.oauth.google.dto.googleInfo;
import commitcapstone.commit.oauth.google.dto.googleOauthAccessToken;
import commitcapstone.commit.oauth.google.dto.googleToken;
import commitcapstone.commit.oauth.google.service.googleService;
import commitcapstone.commit.oauth.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class googleOauthController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(googleOauthController.class);
    private final googleService oauthService;
    private final UserRepository userRepository;

    @Autowired
    public googleOauthController(googleService oauthService, UserRepository userRepository) {
        this.oauthService = oauthService;
        this.userRepository = userRepository;
    }

    @GetMapping("oauth/google/login")
    public ResponseEntity<String> login() {
        String OauthURL = oauthService.createOauthURL();

        return ResponseEntity.ok(OauthURL);
    }

    //todo : email로 회원가입여부를 확인하는게 아닌 id_token의 sub에 저장된 회원 아이디로 비교를 해야함(저장도 마찬가지)
    @GetMapping("oauth/google/redirect")
    public ResponseEntity<googleToken> redirect(@RequestParam String code, HttpSession session) {

        googleToken token = oauthService.getGoogleToken(code);
        googleInfo info = oauthService.getGoogleInfo(token.getAccessToken());



//        boolean isExistingUser = userRepository.existsByEmail(email);
        final String refreshToken = token.getRefreshToken();
//
        //세션에 RefreshToken 저장
        session.setAttribute("refreshToken", token.getRefreshToken());
        LOGGER.info("저장한 리프레시토큰 : " + refreshToken);
        LOGGER.info("세션 아이디 : " + session.getId());
//        googleOauthResponse response = new googleOauthResponse(
//                token.getAccessToken(),
//                isExistingUser //이미 가입된 회원인지 판단 true -> 가입된 회원 false -> 첫 가입 회원
//        );

        return ResponseEntity.ok(token);
    }

    @PostMapping("oauth/google/token")
    public googleOauthAccessToken refresh(HttpSession session) {
        String refreshToken = (String) session.getAttribute("refreshToken");
        LOGGER.info("꺼낸 리프레시토큰 : " + refreshToken);
        LOGGER.info("세션 아이디 : " + session.getId());
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("세션에 저장된 refreshToken 비어있음");
        }
        return oauthService.getGoogleAccesstoken(refreshToken);
    }


}
