package commitcapstone.commit.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    @JsonProperty("refresh_token")
    private String refreshToken;
}
