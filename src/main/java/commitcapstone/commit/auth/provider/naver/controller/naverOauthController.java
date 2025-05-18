    package commitcapstone.commit.auth.provider.naver.controller;


    import commitcapstone.commit.auth.config.jwt.JwtTokenProvider;
    import commitcapstone.commit.auth.provider.naver.dto.naverToken;
    import commitcapstone.commit.auth.provider.naver.service.naverService;
    import jakarta.servlet.http.HttpSession;
    import org.slf4j.LoggerFactory;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.Map;
    import java.util.UUID;

    @RestController
    @RequestMapping("/oauth/naver")
    public class naverOauthController{
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


            return ResponseEntity.ok(Map.of("oauth_access_token" , token.getAccessToken()));
        }


    }

