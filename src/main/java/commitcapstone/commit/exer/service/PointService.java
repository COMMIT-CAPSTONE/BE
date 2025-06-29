package commitcapstone.commit.exer.service;

import commitcapstone.commit.common.code.UserErrorCode;
import commitcapstone.commit.common.exception.UserException;
import commitcapstone.commit.exer.dto.response.BuyResponse;
import commitcapstone.commit.exer.entity.Point;
import commitcapstone.commit.exer.entity.PointType;
import commitcapstone.commit.exer.repository.PointRepository;
import commitcapstone.commit.user.User;
import commitcapstone.commit.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserRepository userRepository;
    private final PointRepository pointRepository;

    public BuyResponse buyGoods(String email, int requiredPoint) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));

        int userPoint = pointRepository.findTotalPointByUserId(user.getId());
        if (userPoint >= requiredPoint) {
            Point point = Point.builder()
                    .user(user)
                    .point(-requiredPoint) // 포인트 차감
                    .type(PointType.BUY)
                    .build();
            pointRepository.save(point);

            int totalPoint = pointRepository.findTotalPointByUserId(user.getId());
            return BuyResponse.builder()
                    .totalPoint(totalPoint)
                    .usePoint(requiredPoint)
                    .build();
        } else {
            throw new UserException(UserErrorCode.NOT_ENOUGH_POINT);
        }
    }


    public int PointCalculate(long min) {
        if (min < 10) return 0;
        else if (min < 20) return 2;
        else if (min < 30) return 4;
        else if (min < 40) return 7;
        else if (min < 50) return 10;
        else if (min < 60) return 13;
        else if (min < 75) return 17;
        else if (min < 90) return 21;
        else if (min < 105) return 26;
        else if (min < 120) return 31;
        else if (min < 150) return 37;
        else if (min < 180) return 45;
        else if (min < 210) return 53;
        else if (min < 240) return 62;
        else if (min < 300) return 72;
        else if (min < 360) return 83;
        else if (min < 420) return 95;
        else if (min < 480) return 108;
        else if (min < 540) return 122;
        else if (min < 600) return 137;
        else if (min < 660) return 153;
        else if (min < 720) return 170;
        else if (min < 780) return 188;
        else if (min < 840) return 207;
        else if (min < 900) return 227;
        else if (min < 960) return 248;
        else if (min < 1020) return 270;
        else if (min < 1080) return 293;
        else if (min < 1140) return 317;
        else if (min < 1200) return 342;
        else if (min < 1260) return 368;
        else if (min < 1320) return 395;
        else if (min < 1380) return 423;
        else if (min < 1440) return 452;
        else return 500;
    }

}
