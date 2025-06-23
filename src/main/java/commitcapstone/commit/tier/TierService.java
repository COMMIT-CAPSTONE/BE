package commitcapstone.commit.tier;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.auth.repository.UserRepository;
import commitcapstone.commit.exer.repository.WorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TierService {

    private final WorkRepository workRepository;
    private final UserRepository userRepository;

    public void updateTier(User user) {
        int userExerTime = workRepository.getTotalDuration(user.getId());
        TierType calculatedTier = getTier(userExerTime);

        TierType nowTier = user.getTier(); // 기존 티어 가져오기

        if (nowTier != calculatedTier) {
            user.setTier(calculatedTier);
            userRepository.save(user);
        }
    }

    public TierType getTier(int min) {
        if (min < 1800) return TierType.헬린이;
        else if (min < 7200) return TierType.헬초보;
        else if (min < 18000) return TierType.헬중수;
        else if (min < 36000) return TierType.헬고수;
        else return TierType.헬신;
    }
}
