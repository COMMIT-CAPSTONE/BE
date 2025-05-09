package commitcapstone.commit.oauth.naver.service;


import commitcapstone.commit.oauth.naver.dto.naverToken;
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

import java.util.UUID;

@Service
public class naverService {
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

    public naverToken getnaverToken(String code, String state) {
        String baseURL = "https://nid.naver.com/oauth2.0/token";
        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .build();

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

    public naverToken getNaverAccesstoken(String refreshToken) {
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
                .bodyToMono(naverToken.class).block();
    }
}
