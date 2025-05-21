package commitcapstone.commit.exer.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TimeStatisticsRequest {

    private String period; //기간 - day, week, month

    @JsonProperty("first_date")
    private LocalDate firstDate; // yyyy-mm-dd

}
