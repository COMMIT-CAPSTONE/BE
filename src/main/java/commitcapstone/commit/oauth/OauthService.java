package commitcapstone.commit.oauth;

import jakarta.servlet.http.HttpSession;

public interface OauthService<T, R, S> {

    public String createOauthURL(HttpSession session);

    public T getToken(HttpSession session, String state, String code);

    public R getReToken(String refereshToken);

    public S verifyToken(String token);
}
