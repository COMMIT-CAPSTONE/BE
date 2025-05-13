package commitcapstone.commit.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Data
public class naverInfo implements OauthUserInfo {

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

    @Override
    public String getId() {
        return response != null ? response.getId() : null;
    }

    @Override
    public String getEmail() {
        return response != null ? response.getEmail() : null;
    }
}
