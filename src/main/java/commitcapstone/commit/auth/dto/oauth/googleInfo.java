package commitcapstone.commit.auth.dto.oauth;

import lombok.Data;

@Data
public class googleInfo implements OauthUserInfo {

    private String sub;
    private String email;

    @Override
    public String getId() {
        return sub;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
