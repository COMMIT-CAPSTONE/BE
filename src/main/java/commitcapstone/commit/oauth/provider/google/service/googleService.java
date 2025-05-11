package commitcapstone.commit.oauth.provider.google.service;


import commitcapstone.commit.oauth.provider.google.controller.googleOauthController;
import commitcapstone.commit.oauth.provider.google.dto.googleValidToken;
import commitcapstone.commit.oauth.provider.google.dto.googleOauthAccessToken;
import commitcapstone.commit.oauth.provider.google.dto.googleToken;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
public class googleService {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(googleOauthController.class);

    @Value("${spring.oauth2.google.client-id}")
    private String clientId;
    @Value("${spring.oauth2.google.client-secret}")
    private String clientSecret;
    @Value("${spring.oauth2.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.oauth2.google.scope[0]}")
    private String scope;




    public String createOauthURL(HttpSession session) {

        String baseURL = "https://accounts.google.com/o/oauth2/v2/auth?";

        String state = UUID.randomUUID().toString();
        session.setAttribute("state", state);

        return UriComponentsBuilder.
                fromUriString(baseURL)
                .queryParam("scope", scope)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .queryParam("state", state)
                .toUriString();

    }

    public googleToken getToken(HttpSession session, String state, String code) {
        String baseURL = "https://oauth2.googleapis.com/token";
        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        String sessionState = (String) session.getAttribute("state");

        if (!state.equals(sessionState)) {
            throw new RuntimeException("google state검증 에러");
        }

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectUri);
        formData.add("client_secret", clientSecret);
        formData.add("state", sessionState);


        return client.post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(googleToken.class).block();
    }


    public googleOauthAccessToken getReToken(String refreshToken) {
        String baseURL = "https://oauth2.googleapis.com/token";
        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();


        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", refreshToken);



        return client.post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(googleOauthAccessToken.class).block();
    }

        public googleValidToken verifyToken(String token) {
        String baseURL = "https://www.googleapis.com/oauth2/v3/userinfo";

        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();

        return client.get()
                .retrieve()
                .bodyToMono(googleValidToken.class)
                .block();
    }
}
