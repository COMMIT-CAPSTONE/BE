package commitcapstone.commit.oauth.google.service;


import commitcapstone.commit.oauth.google.controller.OauthController;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URL;
import java.util.List;
import java.util.Map;

@Service
public class OauthService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OauthController.class);

    @Value("${spring.oauth2.google.client-id}")
    private String clientId;
    @Value("${spring.oauth2.google.client-secret}")
    private String clientSecret;
    @Value("${spring.oauth2.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.oauth2.google.scope[0]}")
    private String scope_1;
    @Value("${spring.oauth2.google.scope[1]}")
    private String scope_2;




    public String createOauthURL() {

        String baseURL = "https://accounts.google.com/o/oauth2/v2/auth?";
        return UriComponentsBuilder.
                fromUriString(baseURL)
                .queryParam("scope",scope_1 + " " + scope_2) // 🔥 한 줄로 붙이기
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .toUriString();

    }
    //todo : scope에서 지정한 정보를 어떻게 어디서 가져오는지 구현 필요.... 2025 - 05 - 07 - am 2:22
//    private Map<String, String> getToken() {
//
//    }
}
