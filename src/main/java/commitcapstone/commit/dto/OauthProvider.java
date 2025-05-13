package commitcapstone.commit.dto;

import lombok.Getter;

@Getter
public enum OauthProvider {
    KAKAO(
            "https://kapi.kakao.com/v2/user/me",
            kakaoInfo.class,
            "application/x-www-form-urlencoded;charset=UTF-8"
    ),

    GOOGLE(
            "https://www.googleapis.com/oauth2/v3/userinfo",
            googleInfo.class,
            "application/x-www-form-urlencoded"
    ),

    NAVER(
            "https://openapi.naver.com/v1/nid/me",
            naverInfo.class,
            "application/x-www-form-urlencoded;charset=UTF-8"
    );

    private final String baseUrl;
    private final Class<? extends OauthUserInfo> responseType;
    private final String headerType;

    OauthProvider(String baseUrl, Class<? extends OauthUserInfo> responseType,  String headerType) {
        this.baseUrl = baseUrl;
        this.responseType = responseType;
        this.headerType = headerType;
    }

}
