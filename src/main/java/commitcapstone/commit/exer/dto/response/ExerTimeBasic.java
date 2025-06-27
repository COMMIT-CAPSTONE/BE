package commitcapstone.commit.exer.dto.response;

import commitcapstone.commit.exer.entity.ExerStatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ExerTimeBasic {
    private ExerStatType type;
    private Long time;
}
