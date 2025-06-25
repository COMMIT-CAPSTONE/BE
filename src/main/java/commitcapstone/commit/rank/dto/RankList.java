package commitcapstone.commit.rank.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RankList {

    List<BaseRankDto> challengeSuccessRanks;
    List<BaseRankDto> havePointRanks;
    List<BaseRankDto> monthExerTimeRanks;
    List<BaseRankDto> weekExerTimeRanks;
    List<BaseRankDto> dailyExerTimeRanks;


}
