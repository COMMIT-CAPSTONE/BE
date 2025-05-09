package commitcapstone.commit.oauth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class kakaoOauthAccessToken {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("token_type")
    private String tokenType;

}
