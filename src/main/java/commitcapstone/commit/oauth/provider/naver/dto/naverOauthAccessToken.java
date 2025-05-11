package commitcapstone.commit.oauth.provider.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class naverOauthAccessToken {

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private long expiresIn;


}
