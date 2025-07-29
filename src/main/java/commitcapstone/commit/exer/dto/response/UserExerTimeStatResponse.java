package commitcapstone.commit.exer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserExerTimeStatResponse {
    private ExerTimeBasic today;
    private ExerTimeBasic total;
    private ExerTimeWithAvg todayStat;
    private ExerTimeWithAvg weekStat;
    private ExerTimeWithAvg monthStat;
    private List<ExerWeekStat> weekStats;
    private LocalDate daily;

    private int weeklyDailyAverageWorkoutDuration; // 이번 주 평균 운동 시간 평균 입장 시간 (분) = (운동 시간들의 총합) ÷ (입장 횟수)
    private int weeklyMaxWorkoutDuration; // 이번 주 최대 운동 시간

}
