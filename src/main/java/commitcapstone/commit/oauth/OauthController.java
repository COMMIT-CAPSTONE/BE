package commitcapstone.commit.oauth;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

public interface OauthController<T, R, S> {

        public ResponseEntity<String> login(HttpSession session);

        public ResponseEntity<T> redirect(@RequestParam String code, String state, HttpSession session);

        public ResponseEntity<R> refresh(HttpSession session);

//        public ResponseEntity logout();

        public ResponseEntity<S> verifyAccessToken(@RequestBody accessTokenRequest request);

}
