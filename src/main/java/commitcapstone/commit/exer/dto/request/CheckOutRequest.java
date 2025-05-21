package commitcapstone.commit.exer.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CheckOutRequest {
    @JsonProperty("min")
    private long Min;

}
