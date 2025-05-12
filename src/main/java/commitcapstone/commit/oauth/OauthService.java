package commitcapstone.commit.oauth;


public interface OauthService<T, R> {

    public String createOauthURL(String state);

    public T getToken(String state, String code);

    public R getUserInfo(String accessToken);

}
