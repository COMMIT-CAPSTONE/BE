package commitcapstone.commit.rank.dto;

import commitcapstone.commit.tier.TierType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class BaseRankDto {
    private int rank;
    private String name;
    private TierType tier;
    private int value;

    private RankType rankType;
}
