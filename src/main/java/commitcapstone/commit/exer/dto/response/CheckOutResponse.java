package commitcapstone.commit.exer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class CheckOutResponse {
    private long min; //추가되는 분
    private int todayTotalTime; //오늘 기록된 분
    private int totalTime; // 총 전체 분

    private int point; //추가되는 포인트
    private int totalPoint; //현재 총 포인트
}
