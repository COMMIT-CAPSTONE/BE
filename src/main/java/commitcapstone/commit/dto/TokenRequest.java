package commitcapstone.commit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenRequest {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("provider")
    private String provider;
}
