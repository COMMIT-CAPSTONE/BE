package commitcapstone.commit.oauth;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.result.view.RedirectView;

import java.util.Map;

public interface OauthController {

        public ResponseEntity<Map<String, String>> login(HttpSession session);

        public ResponseEntity<Map<String, String>> redirect(@RequestParam String code, String state, HttpSession session);

        public ResponseEntity<Map<String, String>> exchange(@RequestBody Map<String, String> body, HttpSession session);
}
