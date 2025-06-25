package commitcapstone.commit.rank.dto;

import commitcapstone.commit.tier.TierType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseRankDto {
    private int rank;
    private String name;
    private TierType tier;
    private Long value;

    public BaseRankDto(String name, TierType tier, Long value) {
        this.name = name;
        this.tier = tier;
        this.value = value;
    }
    private RankType rankType;
}
