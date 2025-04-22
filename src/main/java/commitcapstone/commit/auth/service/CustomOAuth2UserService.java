package commitcapstone.commit.auth.service;


import commitcapstone.commit.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        try {
            OAuth2User oAuth2User = super.loadUser(userRequest);

            String email = oAuth2User.getAttribute("email");
            String name = oAuth2User.getAttribute("name");
            String profileImg = oAuth2User.getAttribute("picture");

            log.info("OAuth2 User - Email: {}", email);
            log.info("OAuth2 User - Name: {}", name);
            log.info("OAuth2 User - ProfileImg: {}", profileImg);

            return oAuth2User;
        } catch (Exception e) {
            log.error("Failed to load OAuth2 user: {}", e.getMessage(), e);
            throw new RuntimeException("failed Oauth2 user load", e);
        }
    }
}
