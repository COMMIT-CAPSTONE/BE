package commitcapstone.commit.oauth.google.service;


import commitcapstone.commit.oauth.google.controller.googleOauthController;
import commitcapstone.commit.oauth.google.dto.googleInfo;
import commitcapstone.commit.oauth.google.dto.googleOauthAccessToken;
import commitcapstone.commit.oauth.google.dto.googleToken;
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




    public String createOauthURL() {

        String baseURL = "https://accounts.google.com/o/oauth2/v2/auth?";
        return UriComponentsBuilder.
                fromUriString(baseURL)
                .queryParam("scope", scope)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("access_type", "offline")
                .queryParam("prompt", "consent")
                .toUriString();

    }
    //todo : scope에서 지정한 정보를 어떻게 어디서 가져오는지 구현 필요.... 2025 - 05 - 07 - am 2:22
    public googleToken getGoogleToken(String code) {
        String baseURL = "https://oauth2.googleapis.com/token";
        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("code", code);
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirectUri);
        formData.add("client_secret", clientSecret);


        return client.post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(googleToken.class).block();
    }

    // todo : 지금 accesstoken 이용해서 api 호출하는 방식으로 email 구하고있는데 이러면 비용발생하니까
    // id_token encoding 해서 email 구해오는 방식으로 리팩토링 필요

    public googleInfo getGoogleInfo(String accessToken) {
        String baseURL = "https://www.googleapis.com/oauth2/v3/userinfo";

        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();

        return client.get()
                .retrieve()
                .bodyToMono(googleInfo.class)
                .block();
    }


    public googleOauthAccessToken getGoogleAccesstoken(String refreshToken) {
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
}
