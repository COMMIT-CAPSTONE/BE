package commitcapstone.commit.oauth.provider.naver.service;


import commitcapstone.commit.oauth.provider.naver.dto.naverToken;
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
public class naverService{
    @Value("${spring.oauth2.naver.client-id}")
    private String clientId;

    @Value("${spring.oauth2.naver.client-secret}")
    private String clientSecret;

    @Value("${spring.oauth2.naver.redirect-url}")
    private String redirectUri;

    @Value("${spring.oauth2.naver.scope[0]}")
    private String scope;

    public String createOauthURL(String state) {
        String baseURL = "https://nid.naver.com/oauth2.0/authorize?";


        return UriComponentsBuilder.
                fromUriString(baseURL)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("state", state)
                .queryParam("auth_type", "reprompt")
                .queryParam("scope", scope)
                .toUriString();
    }

    public naverToken getToken(String state, String code) {
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

}
