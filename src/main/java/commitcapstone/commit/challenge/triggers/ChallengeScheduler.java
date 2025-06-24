package commitcapstone.commit.challenge.triggers;

import commitcapstone.commit.challenge.dto.ChallengeMyProgress;
import commitcapstone.commit.challenge.entity.Challenge;
import commitcapstone.commit.challenge.entity.ChallengeParticipant;
import commitcapstone.commit.challenge.entity.ChallengeType;
import commitcapstone.commit.challenge.repository.ChallengeParticipantRepository;
import commitcapstone.commit.challenge.repository.ChallengeRepository;
import commitcapstone.commit.exer.entity.Point;
import commitcapstone.commit.exer.entity.PointType;
import commitcapstone.commit.exer.repository.PointRepository;
import commitcapstone.commit.exer.repository.WorkRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class ChallengeScheduler {

    private final ChallengeRepository challengeRepository;
    private final ChallengeParticipantRepository participantRepository;
    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final WorkRepository workRepository;
    private final PointRepository pointRepository;

    public ChallengeScheduler(ChallengeRepository challengeRepository, ChallengeParticipantRepository participantRepository, ChallengeParticipantRepository challengeParticipantRepository, WorkRepository workRepository, PointRepository pointRepository) {
        this.challengeRepository = challengeRepository;
        this.participantRepository = participantRepository;
        this.challengeParticipantRepository = challengeParticipantRepository;
        this.workRepository = workRepository;
        this.pointRepository = pointRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void ifChallengeIsEnd() {
        LocalDate now = LocalDate.now();

        List<Challenge> challenges = challengeRepository.findAllByEndDateBeforeAndFinishedFalse(now);

        for (Challenge ch : challenges) {

            List<ChallengeParticipant> successList = new ArrayList<>();
            List<ChallengeParticipant> failList = new ArrayList<>();

            ch.setFinished(true);

            List<ChallengeParticipant> participants = challengeParticipantRepository.findAllByChallenge(ch);
            for (ChallengeParticipant cp : participants) {
                cp.setFinished(true);

                boolean isSuccess = false;

                if (ch.getType() == ChallengeType.TOTAL) {
                    int total = workRepository.getPeriodTotalTimeByUser(cp.getUser().getId(), ch.getStartDate(), ch.getEndDate());
                    isSuccess = total >= ch.getTargetMinutes();

                } else if (ch.getType() == ChallengeType.DAILY) {
                    int totalDays = (int) ChronoUnit.DAYS.between(ch.getStartDate(), ch.getEndDate()) + 1;
                    int successCount = 0;

                    for (LocalDate date = ch.getStartDate(); !date.isAfter(ch.getEndDate()); date = date.plusDays(1)) {
                        int duration = workRepository.getTodayDuration(cp.getUser(), date);
                        if (duration >= ch.getTargetMinutes()) successCount++;
                    }
                    isSuccess = successCount == totalDays;
                }

                if (isSuccess) {
                    successList.add(cp);
                } else {
                    failList.add(cp);
                }
            }

            int successCount = successList.size();

            if (successCount == 0) continue;

            int failCount = failList.size();

            int succesPoint = (int) (ch.getBetPoint() * 1.5) + ((failCount * ch.getBetPoint()) / successCount);

            for (ChallengeParticipant cp : successList) {
                cp.setSuccess(true);

                Point point = Point.builder()
                        .point(succesPoint)
                        .user(cp.getUser())
                        .type(PointType.CHALLENGE_ADD)
                        .earnedAt(LocalDateTime.now())
                        .build();

                pointRepository.save(point);
            }
        }
    }



}
