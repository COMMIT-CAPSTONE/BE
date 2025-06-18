package commitcapstone.commit.challenge.dto;

import commitcapstone.commit.challenge.entity.ChallengeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
public class ChallengeCreateResponse {
    private String challengeTitle;
    private String challengeDescription;
    private ChallengeType challengeType;
    private int betPoint;
    private Integer targetMinutes;
    private LocalDate startDate;
    private LocalDate endDate;
}