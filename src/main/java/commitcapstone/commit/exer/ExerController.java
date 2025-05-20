package commitcapstone.commit.exer;

import commitcapstone.commit.auth.config.jwt.JwtTokenProvider;
import commitcapstone.commit.exer.dto.CheckOutRequest;
import commitcapstone.commit.exer.service.ExerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/exer")
public class ExerController {
    private final JwtTokenProvider jwtTokenProvider;
    private final ExerService exerService;

    public ExerController(JwtTokenProvider jwtTokenProvider, ExerService exerService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.exerService = exerService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckOutRequest checkOutRequest ,@AuthenticationPrincipal String email) {

        exerService.saveTime(email, checkOutRequest.getMin());

        return ResponseEntity.ok("test");
    }

    @GetMapping("/exer-time")
    public ResponseEntity<?> ExerTime(@AuthenticationPrincipal String email) {
        long exerTime = exerService.getExerTime(email);

        return ResponseEntity.ok(Map.of("type", "min", "time", exerTime));
    }
}
