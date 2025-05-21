package commitcapstone.commit.exer;

import commitcapstone.commit.auth.config.jwt.JwtTokenProvider;
import commitcapstone.commit.exer.dto.request.CheckOutRequest;
import commitcapstone.commit.exer.dto.request.TimeStatisticsRequest;
import commitcapstone.commit.exer.dto.response.PeriodDurationResponse;
import commitcapstone.commit.exer.service.ExerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/total-time")
    public ResponseEntity<?> ExerTime(@AuthenticationPrincipal String email) {
        long exerTime = exerService.getExerTime(email);

        return ResponseEntity.ok(Map.of("type", "min", "time", exerTime));
    }

    /*
    * 사용자 운동 시간 통계 관련 api
    *
    * */
    @GetMapping("/statistics")
    public ResponseEntity<?> getTimeStatistics(@AuthenticationPrincipal String email, @RequestBody TimeStatisticsRequest request) {

        List<PeriodDurationResponse> periodDuration =  exerService.getUserExerTime(email, request);

        return ResponseEntity.ok(periodDuration);

    }
}
