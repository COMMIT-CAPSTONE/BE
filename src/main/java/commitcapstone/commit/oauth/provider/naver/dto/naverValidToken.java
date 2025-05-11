package commitcapstone.commit.oauth.provider.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class naverValidToken {

    @JsonProperty("resultcode")
    private String resultcode;

    @JsonProperty("message")
    private String message;
}
