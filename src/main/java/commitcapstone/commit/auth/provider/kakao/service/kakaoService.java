package commitcapstone.commit.auth.provider.kakao.service;


import commitcapstone.commit.auth.provider.kakao.dto.kakaoToken;
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
public class kakaoService{
    @Value("${spring.oauth2.kakao.client-id}")
    private String clientId;

    @Value("${spring.oauth2.kakao.redirect-url}")
    private String redirectUri;

    @Value("${spring.oauth2.kakao.scope[0]}")
    private String scope;

    public String createOauthURL(String state) {
        String baseURL = "https://kauth.kakao.com/oauth/authorize?";

        return UriComponentsBuilder.
                fromUriString(baseURL)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", scope)
                .queryParam("state", state)
                .toUriString();
    }



    public kakaoToken getToken(String state, String code) {
        String baseURL = "https://kauth.kakao.com/oauth/token";
        WebClient client = WebClient.builder()
                .baseUrl(baseURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();



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
}
