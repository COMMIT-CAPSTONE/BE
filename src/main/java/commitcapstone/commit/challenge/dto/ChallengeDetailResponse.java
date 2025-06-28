package commitcapstone.commit.challenge.dto;

import commitcapstone.commit.user.User;
import commitcapstone.commit.challenge.entity.Challenge;
import commitcapstone.commit.challenge.entity.ChallengeType;
import commitcapstone.commit.tier.TierType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ChallengeDetailResponse {
    //챌린지 정보
    private Long challengeId;
    private String challengeTitle;
    private String challengeDescription;
    private ChallengeType challengeType;
    private int challengeImg; // 챌린지 이미지 (int로 변경, 실제 이미지 경로는 프론트에서 처리)
    private int challengeBetPoint;
    private int targetMinutes;
    private int totalAcheiveMinutes;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isFinished;


    private int participants;// 참가한 사용자 수
    private int totalBetPoint;//총 모인 포인트

    private List<ChallengeMyProgress> myProgressList;

    //사용자 정보
    private String userName;
    private TierType tier;
    private int userProfile; // 사용자 프로필 이미지

    public static ChallengeDetailResponse from(Challenge challenge, User user, int participants) {
        return ChallengeDetailResponse.builder()
                .challengeId(challenge.getId())
                .challengeTitle(challenge.getTitle())
                .challengeDescription(challenge.getDescription())
                .challengeType(challenge.getType())
                .challengeImg(challenge.getChallengeImg())
                .challengeBetPoint(challenge.getBetPoint())
                .targetMinutes(challenge.getTargetMinutes())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .isFinished(challenge.isFinished())

                .participants(participants)
                .totalBetPoint(challenge.getBetPoint() * participants)

                .userName(user.getName())
                .tier(user.getTier())
                .userProfile(user.getProfile())
                .build();
    }


}
