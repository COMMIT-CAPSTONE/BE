package commitcapstone.commit.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
@Data
public class UserInfo {

    @JsonProperty("oauth_provider")
    private String oauthProvider;

    @JsonProperty("oauth_id")
    private String oauthId;

    @JsonProperty("email")
    private String email;

    @JsonProperty("nick_name")
    private String nickName;

    @JsonProperty("gym_name")
    private String gymName;

    @JsonProperty("gym_address")
    private String gymAddress;

    @JsonProperty("latitude")
    private double latitude;

    @JsonProperty("longitude")
    private double longitude;

}
