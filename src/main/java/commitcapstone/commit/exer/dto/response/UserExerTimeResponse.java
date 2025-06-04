package commitcapstone.commit.exer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
public class UserExerTimeResponse implements ExerTimeResponse{

    private String type;
    private Integer time;


    @Override
    public String getType() {
        return type;
    }

    @Override
    public Integer getTime() {
        return time;
    }
}
