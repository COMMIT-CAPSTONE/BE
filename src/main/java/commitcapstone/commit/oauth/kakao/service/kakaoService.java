package commitcapstone.commit.oauth.kakao.service;


import commitcapstone.commit.oauth.google.dto.googleOauthAccessToken;
import commitcapstone.commit.oauth.kakao.dto.kakaoOauthAccessToken;
import commitcapstone.commit.oauth.kakao.dto.kakaoToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class kakaoService {
    @Value("${spring.oauth2.kakao.client-id}")
    private String clientId;

    @Value("${spring.oauth2.kakao.redirect-url}")
    private String redirectUri;

    @Value("${spring.oauth2.kakao.scope[0]}")
    private String scope;

    public String createOauthURL() {
        String baseURL = "https://kauth.kakao.com/oauth/authorize?";

        return UriComponentsBuilder.
                fromUriString(baseURL)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", scope + " openid")
                .toUriString();
    }

    public kakaoToken getkakaoToken(String code) {
        String baseURL = "https://kauth.kakao.com/oauth/token";
        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .build();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectUri);




        return client.post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(kakaoToken.class).block();
    }

    public kakaoOauthAccessToken getkakaoAccesstoken(String refreshToken) {
        String baseURL = "https://kauth.kakao.com/oauth/token";
        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
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
}
