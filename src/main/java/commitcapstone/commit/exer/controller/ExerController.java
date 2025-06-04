package commitcapstone.commit.exer.controller;

import commitcapstone.commit.common.response.SuccessResponse;
import commitcapstone.commit.exer.dto.request.CheckOutRequest;
import commitcapstone.commit.exer.dto.response.CheckOutResponse;
import commitcapstone.commit.exer.dto.response.ExerTimeResponse;
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


    @PostMapping("/checkout")
    public ResponseEntity<SuccessResponse<CheckOutResponse>> checkout(@RequestBody CheckOutRequest request , @AuthenticationPrincipal String email) {

        CheckOutResponse response = exerService.saveTimeAndPoint(email, request);

        return ResponseEntity.ok(new SuccessResponse<>("체크아웃 성공", response));
    }

    @GetMapping("time/total")
    public ResponseEntity<SuccessResponse<ExerTimeResponse>> getTotalTime(@AuthenticationPrincipal String email) {
        ExerTimeResponse response = exerService.getExerTime(email, "TOTAL");
        return ResponseEntity.ok(new SuccessResponse<>("전체 시간 호출 성공", response));
    }

    @GetMapping("/time/today")
    public ResponseEntity<SuccessResponse<ExerTimeResponse>> getTodayTime(@AuthenticationPrincipal String email) {
        ExerTimeResponse response = exerService.getExerTime(email, "today");
        return ResponseEntity.ok(new SuccessResponse<>("일일 시간 호출 성공", response));
    }


    @GetMapping("/time/stat/today")
    public ResponseEntity<SuccessResponse<ExerTimeResponse>> getStatTodayTime(@AuthenticationPrincipal String email) {
        ExerTimeResponse response = exerService.getExerTime(email, "TODAY");
        return ResponseEntity.ok(new SuccessResponse<>("일일 운동 시간 비교 통계 호출 성공", response));
    }

    @GetMapping("/time/stat/week")
    public ResponseEntity<SuccessResponse<ExerTimeResponse>> getStatWeekTime(@AuthenticationPrincipal String email) {
        ExerTimeResponse response = exerService.getExerTime(email, "WEEK");
        return ResponseEntity.ok(new SuccessResponse<>("주간 운동 시간 비교 통계 호출 성공", response));
    }

    @GetMapping("/time/stat/month")
    public ResponseEntity<SuccessResponse<ExerTimeResponse>> getStatMonthTime(@AuthenticationPrincipal String email) {
        ExerTimeResponse response = exerService.getExerTime(email, "MONTH");
        return ResponseEntity.ok(new SuccessResponse<>("월간 운동 시간 비교 통계 호출 성공", response));
    }


}
