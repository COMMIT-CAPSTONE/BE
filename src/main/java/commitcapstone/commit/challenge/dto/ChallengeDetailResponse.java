package commitcapstone.commit.challenge.dto;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.challenge.entity.Challenge;
import commitcapstone.commit.challenge.entity.ChallengeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class ChallengeDetailResponse {
    //챌린지 정보
    private Long challengeId;
    private String challengeTitle;
    private String challengeDescription;
    private ChallengeType challengeType;
    private int challengeBetPoint;
    private int targetMinutes;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isFinished;
    
    //추가적인 정보
    private int participant;// 참가한 사용자 수
    private int totalBetPoint;//총 모인 포인트

    //사용자 정보
    private String userName;
    private String tier;

    public static ChallengeDetailResponse from(Challenge challenge, User user) {
        return ChallengeDetailResponse.builder()
                .challengeId(challenge.getId())
                .challengeTitle(challenge.getTitle())
                .challengeDescription(challenge.getDescription())
                .challengeType(challenge.getType())
                .challengeBetPoint(challenge.getBetPoint())
                .targetMinutes(challenge.getTargetMinutes())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .isFinished(challenge.isFinished())

                .userName(user.getName())
                .tier(user.getTier())
                .build();
    }
}
