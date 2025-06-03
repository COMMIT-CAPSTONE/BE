package commitcapstone.commit.exer.service;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.auth.repository.UserRepository;
import commitcapstone.commit.common.code.ExerErrorCode;
import commitcapstone.commit.common.exception.ExerException;
import commitcapstone.commit.exer.dto.request.TimeStatisticsRequest;
import commitcapstone.commit.exer.dto.response.UserTimeResponse;
import commitcapstone.commit.exer.entity.Point;
import commitcapstone.commit.exer.entity.Work;
import commitcapstone.commit.exer.repository.WorkRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class ExerService {
    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    private final PointService pointService;
    public ExerService(UserRepository userRepository, WorkRepository workRepository, PointService pointService) {
        this.userRepository = userRepository;
        this.workRepository = workRepository;
        this.pointService = pointService;
    }

    public UserTimeResponse getExerTime(String email, String type) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));

        LocalDate today = LocalDate.now();
        switch (type) {
            case "TOTAL":
                int totalTime = workRepository.getTotalDuration(user.getId());
                return new UserTimeResponse("TOTAL", totalTime);
            case "TODAY":
                int todayTime = workRepository.getTodayDuration(user.getId(), today);
                return new UserTimeResponse("TODAY", todayTime);
            default:
                throw new ExerException(ExerErrorCode.INVALID_TIME_TYPE);
        }
    }




}
