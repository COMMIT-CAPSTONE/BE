package commitcapstone.commit.oauth.provider.naver.service;


import commitcapstone.commit.oauth.OauthService;
import commitcapstone.commit.oauth.provider.naver.dto.naverOauthAccessToken;
import commitcapstone.commit.oauth.provider.naver.dto.naverToken;
import commitcapstone.commit.oauth.provider.naver.dto.naverValidToken;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class naverService implements OauthService<naverToken, naverOauthAccessToken, naverValidToken> {
    @Value("${spring.oauth2.naver.client-id}")
    private String clientId;

    @Value("${spring.oauth2.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.oauth2.naver.redirect-url}")
    private String redirectUri;

    public String createOauthURL(HttpSession session) {
        String baseURL = "https://nid.naver.com/oauth2.0/authorize?";

        String state = UUID.randomUUID().toString();
        session.setAttribute("state", state);

        return UriComponentsBuilder.
                fromUriString(baseURL)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("state", state)
                .queryParam("auth_type", "reprompt")
                .toUriString();
    }

    public naverToken getToken(HttpSession session, String state, String code) {
        String baseURL = "https://nid.naver.com/oauth2.0/token";
        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .build();

        String sessionState = (String) session.getAttribute("state");

        if (!state.equals(sessionState)) {
            throw new RuntimeException("state검증 에러");
        }

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("state", state);
        formData.add("redirect_uri", redirectUri);


        return client.post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(naverToken.class).block();
    }

    public naverOauthAccessToken getReToken(String refreshToken) {
        String baseURL = "https://nid.naver.com/oauth2.0/token";
        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .build();


        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshToken);



        return client.post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(naverOauthAccessToken.class).block();
    }

    public naverValidToken verifyToken(String token) {
        String baseURL = "https://openapi.naver.com/v1/nid/me";

        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .build();

        return client.post()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(naverValidToken.class).block();
    }

}
