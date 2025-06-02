package commitcapstone.commit.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccessTokenResponse {
    public AccessTokenResponse (String tokenType, String accessToken) {
        token_type = tokenType;
        access_token = accessToken;

    }
    private String access_token;
    private String token_type;
}
