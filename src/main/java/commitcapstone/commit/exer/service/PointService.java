package commitcapstone.commit.exer.service;

import org.springframework.stereotype.Service;

@Service
public class PointService {

    public int PointCalculate(long min) {
        if (min >= 10&&min <= 30) {
            return 10;
        } else if (min <= 60) {
            return 30;
        } else if (min <= 120) {
            return 70;
        } else if (min <= 240) {
            return 100;
        }
        return 0;
    }

}
