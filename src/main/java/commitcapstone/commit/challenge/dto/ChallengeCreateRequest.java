package commitcapstone.commit.challenge.dto;

import commitcapstone.commit.challenge.entity.ChallengeType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ChallengeCreateRequest {

    private String challengeTitle;

    private String challengeDescription;

    private ChallengeType challengeType;

    private int betPoint;

    private int targetMinutes;

    private LocalDate startDate;

    private LocalDate endDate;
}
