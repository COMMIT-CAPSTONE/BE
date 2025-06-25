package commitcapstone.commit.rank.service;

import commitcapstone.commit.challenge.repository.ChallengeRepository;
import commitcapstone.commit.exer.repository.PointRepository;
import commitcapstone.commit.exer.repository.WorkRepository;
import commitcapstone.commit.rank.dto.BaseRankDto;
import commitcapstone.commit.rank.dto.RankList;
import commitcapstone.commit.rank.dto.RankType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RankService {

    private final ChallengeRepository challengeRepository;
    private final PointRepository pointRepository;
    private final WorkRepository workRepository;
    private final PageRequest pageRequest = PageRequest.of(0, 10);
    @Value("${config.start_date}")
    private String appStartDate;

    public RankList getRankList() {
        return RankList.builder()
                .challengeSuccessRanks(getChallengeSuccessRanks().getContent())
                .havePointRanks(getHavePointRanks().getContent())
                .monthExerTimeRanks(getMonthExerTimeRanks().getContent())
                .weekExerTimeRanks(getWeekExerTimeRanks().getContent())
                .dailyExerTimeRanks(dailyExerTimeRanks().getContent())
                .build();
    }

    public Page<BaseRankDto> getRanksByType(RankType rankType, Page<BaseRankDto> list) {

        int i = 0;
        for (BaseRankDto dto : list) {
            dto.setRankType(rankType);
            dto.setRank(++i);
        }

        return list;
    }

    public Page<BaseRankDto> getChallengeSuccessRanks(){

        Page<BaseRankDto> list = challengeRepository.findTopUsersBySuccessCount(pageRequest);
        return getRanksByType(RankType.CHALLENGE_SUCCESS, list);
    }

    public Page<BaseRankDto> getHavePointRanks(){
        Page<BaseRankDto> list = pointRepository.findTopUsersByTotalPoint(pageRequest);
        return getRanksByType(RankType.HAVE_POINT, list);
    }

    public Page<BaseRankDto> getMonthExerTimeRanks() {
        LocalDate start = LocalDate.parse(appStartDate);
        LocalDate end = start.plusDays(30).minusDays(1);
        Page<BaseRankDto> list = workRepository.findExerTimeRankBetween(start, end, pageRequest);
        return getRanksByType(RankType.MONTH_EXER_TIME, list);
    }

    public Page<BaseRankDto> getWeekExerTimeRanks() {
        LocalDate start = LocalDate.parse(appStartDate);
        LocalDate end = start.plusDays(7).minusDays(1);
        Page<BaseRankDto> list = workRepository.findExerTimeRankBetween(start, end, pageRequest);
        return getRanksByType(RankType.WEEK_EXER_TIME, list);
    }

    public Page<BaseRankDto>dailyExerTimeRanks() {
        LocalDate start = LocalDate.parse(appStartDate);
        Page<BaseRankDto> list = workRepository.findExerTimeRankBetween(start, start, pageRequest);
        return getRanksByType(RankType.DAILY_EXER_TIME, list);

    }
}
