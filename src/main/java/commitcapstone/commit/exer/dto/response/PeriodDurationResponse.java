package commitcapstone.commit.exer.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


public interface PeriodDurationResponse {
    LocalDate getDate();
    Long getTotalDuration();
}