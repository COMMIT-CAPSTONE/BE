package commitcapstone.commit.user;

import commitcapstone.commit.common.code.UserErrorCode;
import commitcapstone.commit.common.exception.UserException;
import commitcapstone.commit.exer.repository.PointRepository;
import commitcapstone.commit.exer.repository.WorkRepository;
import commitcapstone.commit.tier.TierType;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    private final UserRepository userRepository;
    private final PointRepository pointRepository;
    private final WorkRepository workRepository;

    public UserService(UserRepository userRepository, PointRepository pointRepository, WorkRepository workRepository) {
        this.userRepository = userRepository;
        this.pointRepository = pointRepository;
        this.workRepository = workRepository;
    }

    public UserInfoResponse getUserInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        String username = user.getName();
        TierType tier = user.getTier();
        int totalPoint = pointRepository.findTotalPointByUserId(user.getId());
        int totalExerTime = workRepository.getTotalDuration(user.getId());
        return new UserInfoResponse(username, tier, totalPoint, totalExerTime);


    }

    public void updateUserName(String email, String newName) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));

        if (newName == null || newName.trim().isEmpty()) {
            throw new UserException(UserErrorCode.INVALID_USER_NAME);
        }

        if (newName.length() > 20 || newName.length() < 2) {
            throw new UserException(UserErrorCode.SIZE_OVER_USER_NAME);
        }

        if (!user.getName().equals(newName) && userRepository.existsByName(newName)) {
            throw new UserException(UserErrorCode.DUPLICATED_USER_NAME);
        }

        user.setName(newName);
        userRepository.save(user);
    }

}
