package commitcapstone.commit.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
public class LoginResponse {
    String tokenType;
    String accessToken;
    String refreshToken;
    String status;

    public LoginResponse(String tokenType, String accessToken, String refreshToken, String status) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.status = status;
    }
}
