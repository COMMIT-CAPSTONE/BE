package commitcapstone.commit.oauth.service;

import commitcapstone.commit.dto.OauthProvider;
import commitcapstone.commit.dto.OauthUserInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OauthService {
    public OauthUserInfo getUserInfo(OauthProvider oauthProvider, String accessToken) {
        WebClient client = WebClient.builder()
                .baseUrl(oauthProvider.getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, oauthProvider.getHeaderType())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .build();

        return client.post()
                .retrieve()
                .bodyToMono(oauthProvider.getResponseType())
                .block();
    }
}