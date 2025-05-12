package commitcapstone.commit.oauth.provider.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Data
public class naverInfo {

    @JsonProperty("resultcode")
    private String resultcode;

    @JsonProperty("message")
    private String message;
    @JsonProperty("response")
    private Response response;
    @Getter
    public static class Response {
        private String id;
        private String email;
    }
}
