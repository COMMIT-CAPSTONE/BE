package commitcapstone.commit.exer;

import commitcapstone.commit.common.response.SuccessResponse;
import commitcapstone.commit.exer.dto.response.UserTimeResponse;
import commitcapstone.commit.exer.service.ExerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/exer")
@RequiredArgsConstructor
public class ExerController {

    private final ExerService exerService;


//    @PostMapping("/checkout")
//    public ResponseEntity<SuccessResponse<ExerTimeResponse>> checkout(@RequestBody CheckOutRequest checkOutRequest , @AuthenticationPrincipal String email) {
//
//        exerService.saveTimeAndPoint(email, checkOutRequest.getMin());
//        long totalExerTime = exerService.getExerTime(email);
//        ExerTimeResponse response = new ExerTimeResponse(totalExerTime);
//
//        return ResponseEntity.ok(new SuccessResponse<>("체크아웃 성공", response));
//    }
//
    @GetMapping("time/total")
    public ResponseEntity<SuccessResponse<UserTimeResponse>> ExerTime(@AuthenticationPrincipal String email) {
        UserTimeResponse exerService.getExerTime(email, "TOTAL");
        return ResponseEntity.ok(new SuccessResponse<>("전체 시간 호출 성공", response));
    }

    @GetMapping("/time/today")
    public ResponseEntity<SuccessResponse<UserTimeResponse>> ExerTime(@AuthenticationPrincipal String email) {
        UserTimeResponse exerService.getExerTime(email, "TODAY");
        return ResponseEntity.ok(new SuccessResponse<>("전체 시간 호출 성공", response));
    }
//
//    /*
//    * 사용자 운동 시간 통계 관련 api
//    *
//    * */


//    @GetMapping("/statistics")
//    public ResponseEntity<?> getTimeStatistics(@AuthenticationPrincipal String email, @RequestBody TimeStatisticsRequest request) {
//
//        List<PeriodDurationResponse> periodDuration =  exerService.getUserExerTime(email, request);
//
//        return ResponseEntity.ok(periodDuration);
//
//    }
}
