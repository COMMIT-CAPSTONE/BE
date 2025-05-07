package commitcapstone.commit.oauth.google.dto;


import lombok.Data;

@Data
public class googleOauthResponse {

    private String accessToken;
    private boolean isExistingUser;


    public googleOauthResponse(String accessToken, boolean isExistingUser) {
        this.accessToken = accessToken;
        this.isExistingUser = isExistingUser;
    }
}
