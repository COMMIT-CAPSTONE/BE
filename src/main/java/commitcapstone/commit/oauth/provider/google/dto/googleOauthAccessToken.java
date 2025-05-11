package commitcapstone.commit.oauth.provider.google.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class googleOauthAccessToken {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("id_token")
    private String idToken;
}
