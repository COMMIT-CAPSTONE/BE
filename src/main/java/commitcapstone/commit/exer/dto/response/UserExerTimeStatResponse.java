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
}
