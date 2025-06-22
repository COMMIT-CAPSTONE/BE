package commitcapstone.commit.exer.dto.response;


import commitcapstone.commit.exer.entity.ExerStatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExerTimeWithAvg {
    private ExerStatType type;
    private Long time;
    private Long avgTime;
}