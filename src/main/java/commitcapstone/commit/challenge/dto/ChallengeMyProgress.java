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
public class ChallengeMyProgress {
    private LocalDate day;
    private int targetMin;
    private int achieveMin;
    private ChallengeType type;
    private boolean success;//today인 경우에만 사용
    private boolean isToday;

    public static ChallengeMyProgress from(LocalDate day, int targetMin, int achieveMin, boolean isToday) {
        return ChallengeMyProgress.builder()
                .day(day)
                .targetMin(targetMin)
                .achieveMin(achieveMin)
                .success(achieveMin >= targetMin)
                .isToday(isToday)
                .build();
    }
}
