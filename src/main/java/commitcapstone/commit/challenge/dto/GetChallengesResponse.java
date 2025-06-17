package commitcapstone.commit.challenge.dto;

import commitcapstone.commit.challenge.entity.ChallengeType;
import commitcapstone.commit.challenge.entity.Challenge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class GetChallengesResponse {
    private long challengeId;
    private String challengeTitle;
    private String challengeDescription;
    private ChallengeType challengeType;
    private int challengeBetPoint;

    public static GetChallengesResponse from(Challenge challenge) {
        return GetChallengesResponse.builder()
                .challengeId(challenge.getId())
                .challengeTitle(challenge.getTitle())
                .challengeDescription(challenge.getDescription())
                .challengeType(challenge.getType())
                .challengeBetPoint(challenge.getBetPoint())
                .build();
    }
}
