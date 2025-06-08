package commitcapstone.commit.exer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserExerTimeStatResponse implements ExerTimeResponse{

    private String type;
    private Integer time;
    private Integer avgTime;


    @Override
    public String getType() {
        return type;
    }

    @Override
    public Integer getTime() {
        return time;
    }
}
