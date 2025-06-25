package commitcapstone.commit.challenge.utils;

import commitcapstone.commit.challenge.entity.ChallengeSortType;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class Utils {

    public Sort getSort(ChallengeSortType sortType) {
        if (sortType == null) {
            return Sort.by(Sort.Direction.ASC, "startDate");
        }

        switch (sortType) {
            case BETTING_ASC:
                return Sort.by(Sort.Direction.ASC, "betPoint");
            case BETTING_DESC:
                return Sort.by(Sort.Direction.DESC, "betPoint");
            case TARGET_MIN_ASC:
                return Sort.by(Sort.Direction.ASC, "targetMinutes");
            case TARGET_MIN_DESC:
                return Sort.by(Sort.Direction.DESC, "targetMinutes");
            default:
                return Sort.by(Sort.Direction.ASC, "startDate");
        }
    }

}
