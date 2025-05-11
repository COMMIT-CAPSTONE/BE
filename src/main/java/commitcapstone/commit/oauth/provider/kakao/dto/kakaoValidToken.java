package commitcapstone.commit.oauth.provider.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class kakaoValidToken {
    @JsonProperty("id")
    private String id;

    @JsonProperty("expires_in")
    private Integer expiresIn;

    @JsonProperty("app_id")
    private Integer appId;
}
