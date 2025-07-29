package commitcapstone.commit.exer.dto.response;


import commitcapstone.commit.exer.entity.ExerStatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ExerTimeWithAvg {
    private LocalDate startDate;
    private LocalDate endDate;
    private ExerStatType type;
    private Long time;
    private Long avgTime;
}