package commitcapstone.commit.auth.service;

import commitcapstone.commit.auth.dto.oauth.OauthProvider;
import commitcapstone.commit.auth.dto.oauth.OauthUserInfo;
import commitcapstone.commit.auth.repository.UserRepository;
import lombok.val;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OauthService {

    private final UserRepository userRepository;

    public OauthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    public boolean isUserCheck(String provider, String email) {
        if (userRepository.existProviderAndEmail(provider,email)) {
            return true;
        } else {
            return false;
        }
    }
}