package commitcapstone.commit.oauth.provider.google.service;
import commitcapstone.commit.oauth.OauthController;
import commitcapstone.commit.oauth.OauthService;
import commitcapstone.commit.oauth.provider.google.dto.googleInfo;
import commitcapstone.commit.oauth.provider.google.dto.googleToken;
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
public class googleService implements OauthService<googleToken, googleInfo> {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(OauthController.class);

    @Value("${spring.oauth2.google.client-id}")
    private String clientId;
    @Value("${spring.oauth2.google.client-secret}")
    private String clientSecret;
    @Value("${spring.oauth2.google.redirect-uri}")
    private String redirectUri;
    @Value("${spring.oauth2.google.scope[0]}")
    private String scope;




    public String createOauthURL(String state) {

        String baseURL = "https://accounts.google.com/o/oauth2/v2/auth?";

        return UriComponentsBuilder.
                fromUriString(baseURL)
                .queryParam("scope", scope)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("state", state)
                .toUriString();

    }

    public googleToken getToken(String state, String code) {
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
        formData.add("state", state);


        return client.post()
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(googleToken.class).block();
    }

    public googleInfo getUserInfo(String accessToken) {
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


}
