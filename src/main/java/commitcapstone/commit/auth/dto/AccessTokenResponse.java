package commitcapstone.commit.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccessTokenResponse {
    public AccessTokenResponse (String accessToken, String tokenType) {
        access_token = accessToken;
        token_type = tokenType;
    }
    private String access_token;
    private String token_type;
}
