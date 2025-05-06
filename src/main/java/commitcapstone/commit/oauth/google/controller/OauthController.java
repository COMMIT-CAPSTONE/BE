package commitcapstone.commit.oauth.google.controller;

import commitcapstone.commit.oauth.google.service.OauthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthController {
    private final OauthService oauthService;

    public OauthController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("oauth/login")
    public ResponseEntity<String> login() {
        String OauthURL = oauthService.createOauthURL();


        return ResponseEntity.ok(OauthURL);
    }
}
