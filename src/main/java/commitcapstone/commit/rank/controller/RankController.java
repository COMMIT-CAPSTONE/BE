package commitcapstone.commit.rank.controller;

import commitcapstone.commit.common.response.SuccessResponse;
import commitcapstone.commit.rank.dto.RankList;
import commitcapstone.commit.rank.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class RankController {

    private final RankService rankService;

    @GetMapping("/rank")
    public ResponseEntity<SuccessResponse<RankList>> getRankList() {
        RankList response = rankService.getRankList();
        return ResponseEntity.ok(new SuccessResponse<>("랭킹 불러오기 성공", response));
    }
}
