package commitcapstone.commit.exer.service;

import commitcapstone.commit.user.User;
import commitcapstone.commit.user.UserRepository;

import commitcapstone.commit.exer.dto.request.CheckOutRequest;
import commitcapstone.commit.exer.dto.response.*;
import commitcapstone.commit.exer.entity.ExerStatType;
import commitcapstone.commit.exer.entity.Point;
import commitcapstone.commit.exer.entity.PointType;
import commitcapstone.commit.exer.entity.Work;
import commitcapstone.commit.exer.repository.PointRepository;
import commitcapstone.commit.exer.repository.WorkRepository;
import commitcapstone.commit.tier.TierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;


@Service
@Slf4j
public class ExerService {
    private final UserRepository userRepository;
    private final WorkRepository workRepository;
    private final PointService pointService;
    private final PointRepository pointRepository;
    private final TierService tierService;

    @Value("${config.start_date}")
    private String startDate;


    public ExerService(UserRepository userRepository, WorkRepository workRepository, PointService pointService, PointRepository pointRepository, TierService tierService) {
        this.userRepository = userRepository;
        this.workRepository = workRepository;
        this.pointService = pointService;
        this.pointRepository = pointRepository;
        this.tierService = tierService;
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
        int todayTotalTime = workRepository.getTodayDuration(user, today);
        int totalTime = workRepository.getTotalDuration(user.getId());
        int totalPoint = pointRepository.findTotalPointByUserId(user.getId());

        tierService.updateTier(user);

        return new CheckOutResponse(min, todayTotalTime, totalTime, addPoint, totalPoint);
    }
    public UserExerTimeStatResponse getExerTime(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));

        LocalDate today = LocalDate.now();
        LocalDate weekStartDate = getWeekStartDate();
        LocalDate weekEndDate = weekStartDate.plusDays(6);
        LocalDate monthStartDate = getMonthStartDate();
        LocalDate monthEndDate = monthStartDate.plusDays(29);


        long totalTime = workRepository.getTotalDuration(user.getId());
        long todayTime = workRepository.getTodayDuration(user, today);
        long todayAvg = workRepository.getPeriodToTalTimeBuOtherUsers(user.getId(), today, today);

        long weekTime = workRepository.getPeriodTotalTimeByUser(user.getId(), weekStartDate, weekEndDate);
        long weekAvg = workRepository.getPeriodToTalTimeBuOtherUsers(user.getId(), weekStartDate, weekEndDate);

        long monthTime = workRepository.getPeriodTotalTimeByUser(user.getId(), monthStartDate, monthEndDate);
        long monthAvg = workRepository.getPeriodToTalTimeBuOtherUsers(user.getId(), monthStartDate, monthEndDate);


        List<ExerWeekStat> weekStats = getFullDailyStats(user, weekStartDate, weekEndDate);

        int weeklyDailyAverageWorkoutDuration = (int) Math.round(workRepository.getAverageWorkoutDurationByUser(user.getId(), weekStartDate, weekEndDate));
        int weeklyMaxWorkoutDuration = workRepository.getMaxWorkoutDurationByUser(user.getId(), weekStartDate, weekEndDate); // 이번 주 최대 운동 시간

        return new UserExerTimeStatResponse(
                new ExerTimeBasic(ExerStatType.USER_TODAY, todayTime),
                new ExerTimeBasic(ExerStatType.USER_TOTAL, totalTime),
                new ExerTimeWithAvg(today, today, ExerStatType.TODAY, todayTime, todayAvg),
                new ExerTimeWithAvg(weekStartDate, weekEndDate, ExerStatType.WEEK, weekTime, weekAvg),
                new ExerTimeWithAvg(monthStartDate, monthEndDate, ExerStatType.MONTH, monthTime, monthAvg),
                weekStats,
                LocalDate.now(),
                weeklyDailyAverageWorkoutDuration,
                weeklyMaxWorkoutDuration






        );

    }public List<ExerWeekStat> getFullDailyStats(User user, LocalDate start, LocalDate end) {

        List<ExerWeekStat> partialStats = workRepository.findDailyDurationsGrouped(user, start, end);


        Map<LocalDate, Long> statMap = partialStats.stream()
                .collect(Collectors.toMap(ExerWeekStat::getWorkDate, ExerWeekStat::getDuration));


        List<ExerWeekStat> fullStats = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            long duration = statMap.getOrDefault(date, 0L);
            fullStats.add(new ExerWeekStat(date, duration));
        }

        return fullStats;
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
