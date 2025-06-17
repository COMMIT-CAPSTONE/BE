package commitcapstone.commit.challenge.controller;

import commitcapstone.commit.challenge.dto.GetChallengesResponse;
import commitcapstone.commit.challenge.dto.PostChallengeRequest;
import commitcapstone.commit.challenge.dto.PostChallengeResponse;
import commitcapstone.commit.challenge.entity.Challenge;
import commitcapstone.commit.challenge.entity.ChallengeSortType;
import commitcapstone.commit.challenge.entity.ChallengeType;
import commitcapstone.commit.challenge.service.ChallengeService;
import commitcapstone.commit.common.response.SuccessResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    //챌린지 생성
    @PostMapping("/challenges")
    public ResponseEntity<SuccessResponse<PostChallengeResponse>> postChallenge(@AuthenticationPrincipal String email, @Valid @RequestBody PostChallengeRequest request) {
        PostChallengeResponse response = challengeService.saveChallenge(email, request);
        return ResponseEntity.ok(new SuccessResponse<>("챌린지 생성 성공", response));
    }

    //기본적으로 챌린지 페이지에 나타내어지는 챌린지 리스트
    @GetMapping("/challenges")
    public ResponseEntity<SuccessResponse<Page<GetChallengesResponse>>> getChallenges(
            @RequestParam(name = "type", required = false, defaultValue = "ALL") ChallengeType type,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) ChallengeSortType sort) {

        Page<GetChallengesResponse> response = challengeService.getChallenges(type, keyword, page, size, sort);
        return ResponseEntity.ok(new SuccessResponse<>("챌린지 조회 성공", response));
    }


}
