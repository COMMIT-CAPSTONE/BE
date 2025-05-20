package commitcapstone.commit.exer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class CheckOutRequest {
    @JsonProperty("min")
    private long Min;

}
