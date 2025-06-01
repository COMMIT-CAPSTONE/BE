package commitcapstone.commit.auth.dto.response;


public class LoginResponse {
    String tokenType;
    String accessToken;
    String refreshToken;
    String status;

    public LoginResponse(String bearer, String accessToken, String refreshToken, String status) {
    }
}
