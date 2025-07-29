package commitcapstone.commit.exer.controller;

import commitcapstone.commit.common.response.SuccessResponse;
import commitcapstone.commit.exer.dto.request.BuyRequest;
import commitcapstone.commit.exer.dto.response.BuyResponse;
import commitcapstone.commit.exer.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/point")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;

    @PostMapping("/buy")
    public ResponseEntity<SuccessResponse<BuyResponse>> buyPoint(@AuthenticationPrincipal String email, BuyRequest request) {
        return ResponseEntity.ok(new SuccessResponse<>("상품 구매 성공", pointService.buyGoods(email, request.getPoint())));
    }
}
