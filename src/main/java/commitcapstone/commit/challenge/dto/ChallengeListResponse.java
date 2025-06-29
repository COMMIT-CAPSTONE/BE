package commitcapstone.commit.challenge.dto;

import commitcapstone.commit.challenge.entity.ChallengeType;
import commitcapstone.commit.challenge.entity.Challenge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class ChallengeListResponse {
    private Long challengeId;
    private String challengeTitle;
    private String challengeDescription;
    private ChallengeType challengeType;
    private int challengeBetPoint;
    private int targetMinutes;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isFinished;

    public static ChallengeListResponse from(Challenge challenge) {
        return ChallengeListResponse.builder()
                .challengeId(challenge.getId())
                .challengeTitle(challenge.getTitle())
                .challengeDescription(challenge.getDescription())
                .challengeType(challenge.getType())
                .challengeBetPoint(challenge.getBetPoint())
                .targetMinutes(challenge.getTargetMinutes())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .isFinished(challenge.isFinished())
                .build();
    }

}
