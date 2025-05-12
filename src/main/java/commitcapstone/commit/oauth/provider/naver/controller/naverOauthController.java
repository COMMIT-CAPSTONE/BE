    package commitcapstone.commit.oauth.provider.naver.controller;


    import commitcapstone.commit.oauth.OauthController;
    import commitcapstone.commit.oauth.config.jwt.JwtTokenProvider;
    import commitcapstone.commit.oauth.provider.naver.dto.naverInfo;
    import commitcapstone.commit.oauth.provider.naver.dto.naverToken;
    import commitcapstone.commit.oauth.provider.naver.service.naverService;
    import jakarta.servlet.http.HttpSession;
    import org.slf4j.LoggerFactory;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;
    import org.springframework.web.reactive.result.view.RedirectView;

    import java.util.Map;
    import java.util.UUID;

    @RestController
    @RequestMapping("/oauth/naver")
    public class naverOauthController implements OauthController {
        private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(naverOauthController.class);
        private final naverService oauthService;
        private final JwtTokenProvider jwtTokenProvider;
        public naverOauthController(naverService oauthService, JwtTokenProvider jwtTokenProvider) {
            this.oauthService = oauthService;
            this.jwtTokenProvider = jwtTokenProvider;
        }

        /*
        * 로그인 경로 반환
        * todo: 로그인 경로로 바로 리다이렉트
        * */
        @GetMapping("/login")
        public ResponseEntity<Map<String, String>> login(HttpSession session) {

            String state = UUID.randomUUID().toString();
            session.setAttribute("state", state);

            String OauthURL = oauthService.createOauthURL(state);
            return ResponseEntity.ok(Map.of("oauth_url", OauthURL));

        }


        @GetMapping("/redirect")
        public ResponseEntity<Map<String , String>> redirect(@RequestParam String code, String state, HttpSession session) {
            String sessionState = (String) session.getAttribute("state");

            if (sessionState == null || !sessionState.equals(state)) {
                throw new IllegalStateException("Invalid naverOauth state");
            }
            // naver에 토큰 요청
            naverToken token = oauthService.getToken(state, code);
            // naver에 사용자 정보 요청 (email과 고유 ID)
            naverInfo userInfo = oauthService.getUserInfo(token.getAccessToken());

            String email = userInfo.getResponse().getEmail();
            LOGGER.info("이메일 정보 획득: "  + email);
            // 자체 JWT 생성
            String accessToken = jwtTokenProvider.createAccessToken(email);
            String refreshToken = jwtTokenProvider.createRefreshToken(email);

            String sessionId = UUID.randomUUID().toString();
            session.setAttribute(sessionId, accessToken);

            session.setAttribute("refreshToken", refreshToken);




            return ResponseEntity.ok(Map.of("session_id" , sessionId));
        }

        @PostMapping("/exchange")
        public ResponseEntity<Map<String, String>> exchange(@RequestBody Map<String, String> body, HttpSession session) {
            String sessionId = body.get("session_id");

            if (sessionId == null) {
                throw new RuntimeException("naver Oauth exchange session_id is NULL");
            }

            String accessToken = (String) session.getAttribute(sessionId);
            if (accessToken == null) {
                throw new RuntimeException("naver Oauth exchange accessToken is NULL");
            }


//            //todo: 토큰 검증 구현 필요
//            if(jwtTokenProvider.validateToken(accessToken)) {
//
//            }

            session.removeAttribute("sessionCode");

            return ResponseEntity.ok(Map.of("accessToken", accessToken));
        }





    }

