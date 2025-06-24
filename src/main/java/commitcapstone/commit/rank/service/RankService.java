package commitcapstone.commit.rank.service;

import commitcapstone.commit.challenge.repository.ChallengeRepository;
import commitcapstone.commit.exer.repository.PointRepository;
import commitcapstone.commit.exer.repository.WorkRepository;
import commitcapstone.commit.rank.dto.BaseRankDto;
import commitcapstone.commit.rank.dto.RankList;
import commitcapstone.commit.rank.dto.RankType;
import commitcapstone.commit.tier.TierType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankService {

    private final ChallengeRepository challengeRepository;
    private final PointRepository pointRepository;
    private final WorkRepository workRepository;

    @Value("${config.start_date}")
    private String appStartDate;

    public RankList getRankList() {
        return RankList.builder()
                .challengeSuccessRanks(getChallengeSuccessRanks())
                .havePointRanks(getHavePointRanks())
                .monthExerTimeRanks(getMonthExerTimeRanks())
                .weekExerTimeRanks(getWeekExerTimeRanks())
                .dailyExerTimeRanks(dailyExerTimeRanks())
                .build();
    }

    public List<BaseRankDto> getRanksByType(RankType rankType, List<Object[]> list) {
        List<BaseRankDto> rankList = new ArrayList<>();
        for (Object[] obj : list) {
            BaseRankDto rankDto = BaseRankDto.builder()
                    .rank((int) obj[0])
                    .name((String) obj[1])
                    .tier((TierType) obj[2])
                    .value((int) obj[3])
                    .rankType(rankType)
                    .build();

            rankList.add(rankDto);
        }
        return rankList;
    }

    public List<BaseRankDto> getChallengeSuccessRanks(){
        List<Object[]> list = challengeRepository.findTop10UsersBySuccessCountNative();
        return getRanksByType(RankType.CHALLENGE_SUCCESS, list);
    }

    public List<BaseRankDto> getHavePointRanks(){
        List<Object[]> list = pointRepository.findTop10UsersByTotalPoint();
        return getRanksByType(RankType.HAVE_POINT, list);
    }

    public List<BaseRankDto> getMonthExerTimeRanks() {
        LocalDate start = LocalDate.parse(appStartDate);
        LocalDate end = start.plusDays(30).minusDays(1);
        List<Object[]> list = workRepository.findExerTimeRankBetween(start, end);
        return getRanksByType(RankType.MONTH_EXER_TIME, list);
    }

    public List<BaseRankDto> getWeekExerTimeRanks() {
        LocalDate start = LocalDate.parse(appStartDate);
        LocalDate end = start.plusDays(7).minusDays(1);
        List<Object[]> list = workRepository.findExerTimeRankBetween(start, end);
        return getRanksByType(RankType.WEEK_EXER_TIME, list);
    }

    public List<BaseRankDto>dailyExerTimeRanks() {
        LocalDate start = LocalDate.parse(appStartDate);
        List<Object[]> list = workRepository.findExerTimeRankBetween(start, start);
        return getRanksByType(RankType.DAILY_EXER_TIME, list);

    }
}
