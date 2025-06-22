package commitcapstone.commit.exer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ExerWeekStat {
    private LocalDate workDate;
    private Long duration;
}