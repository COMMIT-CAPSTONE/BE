package commitcapstone.commit.exer.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "체크아웃 요청 정보를 담는 DTO")
@Setter
@Getter
public class CheckOutRequest {

    @Schema(description = "최소값", example = "1", required = true)
    private int min;

}
