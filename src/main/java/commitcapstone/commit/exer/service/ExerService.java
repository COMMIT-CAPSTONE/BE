package commitcapstone.commit.exer.service;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.auth.repository.UserRepository;
import commitcapstone.commit.challenge.entity.Challenge;
import commitcapstone.commit.challenge.repository.ChallengeRepository;
import commitcapstone.commit.common.code.ExerErrorCode;
import commitcapstone.commit.common.exception.ExerException;

import commitcapstone.commit.exer.dto.request.CheckOutRequest;
import commitcapstone.commit.exer.dto.response.CheckOutResponse;
import commitcapstone.commit.exer.dto.response.ExerTimeResponse;
import commitcapstone.commit.exer.dto.response.UserExerTimeResponse;
import commitcapstone.commit.exer.dto.response.UserExerTimeStatResponse;
import commitcapstone.commit.exer.entity.Point;
import commitcapstone.commit.exer.entity.PointType;
import commitcapstone.commit.exer.entity.Work;
import commitcapstone.commit.exer.repository.PointRepository;
import commitcapstone.commit.exer.repository.WorkRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;


@Service
@Slf4j
public class ExerService {
    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    private final PointService pointService;
    private final PointRepository pointRepository;
    private final ChallengeRepository challengeRepository;

    @Value("${config.start_date}")
    private String startDate;


    public ExerService(UserRepository userRepository, WorkRepository workRepository, PointService pointService, PointRepository pointRepository, ChallengeRepository challengeRepository) {
        this.userRepository = userRepository;
        this.workRepository = workRepository;
        this.pointService = pointService;
        this.pointRepository = pointRepository;
        this.challengeRepository = challengeRepository;
    }

    @Transactional
    public CheckOutResponse saveTimeAndPoint(String email, CheckOutRequest request){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));

        long min = request.getMin();
        LocalDate today = LocalDate.now();
        int addPoint = pointService.PointCalculate(min);

        Work work = Work.builder()
                .user(user)
                .duration(min)
                .workDate(today).build();

//        if(!challengeRepository.existsByOwnerAndIsFinishedFalse(user)) {
//            work.builder().challenge();
//        } 보니까 이거는 오너인 경우에만 찾는거임 지금 필요한거는 참가자 테이블.


        Point point = Point.builder()
                .user(user)
                .point(addPoint)
                .type(PointType.EXER_ADD)
                .build();

        workRepository.save(work);
        pointRepository.save(point);

        log.info("addPoint : " + addPoint);
        int todayTotalTime = workRepository.getTodayDuration(user.getId(), today);
        int totalTime = workRepository.getTotalDuration(user.getId());
        int totalPoint = pointRepository.findTotalPointByUserId(user.getId());


        return new CheckOutResponse(min, todayTotalTime, totalTime, addPoint, totalPoint);
    }
    public ExerTimeResponse getExerTime(String email, String type) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
        LocalDate today = LocalDate.now();
        switch (type) {
            case "TOTAL":
                int totalTime = workRepository.getTotalDuration(user.getId());
                return new UserExerTimeResponse("TOTAL", totalTime);
            case "TODAY":
                int statTodayTime = workRepository.getTodayDuration(user.getId(), today);
                int todayTimeAvg = workRepository.getPeriodToTalTimeBuOtherUsers(user.getId(), today, today);
                return new UserExerTimeStatResponse("TODAY", statTodayTime, todayTimeAvg);
            case "today":
                int todayTime = workRepository.getTodayDuration(user.getId(), today);
                return new UserExerTimeResponse("today", todayTime);
            case "WEEK":
                LocalDate weekStartDate = getWeekStartDate();
                LocalDate weekEndDate = getWeekStartDate().plusDays(6);
                int weekTime = workRepository.getPeriodTotalTimeByUser(user.getId(), weekStartDate, weekEndDate);

                int weekTimeAvg = workRepository.getPeriodToTalTimeBuOtherUsers(user.getId(), weekStartDate, weekEndDate);
                return new UserExerTimeStatResponse("WEEK", weekTime, weekTimeAvg);
            case "MONTH":
                LocalDate monthStartDate = getMonthStartDate();
                LocalDate monthEndDate = getMonthStartDate().plusDays(29);
                int monthTime = workRepository.getPeriodTotalTimeByUser(user.getId(), monthStartDate, monthEndDate);

                int monthTimeAvg = workRepository.getPeriodToTalTimeBuOtherUsers(user.getId(), monthStartDate, monthEndDate);
                return new UserExerTimeStatResponse("MONTH", monthTime, monthTimeAvg);
            default:
                throw new ExerException(ExerErrorCode.INVALID_TIME_TYPE);
        }
    }

    public LocalDate getWeekStartDate() {
        LocalDate now = LocalDate.now();
        LocalDate appStartDate = LocalDate.parse(startDate);

        long days = DAYS.between(appStartDate, now);

        LocalDate weekStartDate = appStartDate.plusWeeks(days/7);


        return weekStartDate;
    }

    public LocalDate getMonthStartDate() {
        LocalDate now = LocalDate.now();
        LocalDate appStartDate = LocalDate.parse(startDate);

        long days = DAYS.between(appStartDate, now);

        LocalDate weekStartDate = appStartDate.plusWeeks(days/30);


        return weekStartDate;
    }


}
