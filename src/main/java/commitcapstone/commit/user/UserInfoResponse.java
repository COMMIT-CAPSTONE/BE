package commitcapstone.commit.user;

import commitcapstone.commit.tier.TierType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private String name;
    private TierType tier;
    private int totalPoint;
    private int totalExerTime;
    private int profile;
}
