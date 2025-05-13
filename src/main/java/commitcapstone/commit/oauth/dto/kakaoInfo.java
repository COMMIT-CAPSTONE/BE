package commitcapstone.commit.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

@Data
public class kakaoInfo implements OauthUserInfo {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    public static class KakaoAccount {
        @JsonProperty("email")
        private String email;
    }

    @Override
    public String getId() {
        return id != null ? id.toString() : null;
    }

    @Override
    public String getEmail() {
        return kakaoAccount != null ? kakaoAccount.getEmail() : null;
    }
}
