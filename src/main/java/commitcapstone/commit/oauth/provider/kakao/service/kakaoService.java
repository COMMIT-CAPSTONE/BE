package commitcapstone.commit.oauth.provider.kakao.service;


import commitcapstone.commit.oauth.OauthService;
import commitcapstone.commit.oauth.provider.kakao.dto.kakaoOauthAccessToken;
import commitcapstone.commit.oauth.provider.kakao.dto.kakaoToken;
import commitcapstone.commit.oauth.provider.kakao.dto.kakaoValidToken;
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

import java.util.UUID;

@Service
public class kakaoService implements OauthService<kakaoToken, kakaoOauthAccessToken, kakaoValidToken> {
    @Value("${spring.oauth2.kakao.client-id}")
    private String clientId;

    @Value("${spring.oauth2.kakao.redirect-url}")
    private String redirectUri;

    @Value("${spring.oauth2.kakao.scope[0]}")
    private String scope;

    public String createOauthURL(HttpSession session) {
        String baseURL = "https://kauth.kakao.com/oauth/authorize?";

        String state = UUID.randomUUID().toString();
        session.setAttribute("state", state);

        return UriComponentsBuilder.
                fromUriString(baseURL)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", scope + " openid")
                .queryParam("state", state)
                .toUriString();
    }



    public kakaoToken getToken(HttpSession session, String state, String code) {
        String baseURL = "https://kauth.kakao.com/oauth/token";
        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();


        String sessionState = (String) session.getAttribute("state");

        if (!state.equals(sessionState)) {
            throw new RuntimeException("state검증 에러");
        }


        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectUri);
        formData.add("state", state);




        return client.post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(kakaoToken.class).block();
    }

    public kakaoOauthAccessToken getReToken(String refreshToken) {
        String baseURL = "https://kauth.kakao.com/oauth/token";
        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();


        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("grant_type", "refresh_token");
        formData.add("refresh_token", refreshToken);



        return client.post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(kakaoOauthAccessToken.class).block();
    }
    public kakaoValidToken verifyToken(String token) {
        String baseURL = "https://kapi.kakao.com/v1/user/access_token_info";

        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return client.post()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(kakaoValidToken.class).block();
    }
}
