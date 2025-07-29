package commitcapstone.commit.exer.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BuyRequest {
    @Schema(description = "구매에 사용할 포인트", example = "1000")
    private int point;
}