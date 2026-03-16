package commitcapstone.commit.challenge.triggers;

import commitcapstone.commit.challenge.entity.Challenge;
import commitcapstone.commit.challenge.entity.ChallengeParticipant;
import commitcapstone.commit.challenge.entity.ChallengeType;
import commitcapstone.commit.challenge.repository.ChallengeParticipantRepository;
import commitcapstone.commit.challenge.repository.ChallengeRepository;
import commitcapstone.commit.exer.entity.Point;
import commitcapstone.commit.exer.entity.PointType;
import commitcapstone.commit.exer.repository.PointRepository;
import commitcapstone.commit.exer.repository.WorkRepository;
import commitcapstone.commit.notification.entity.NotificationType;
import commitcapstone.commit.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class ChallengeScheduler {

    private final ChallengeRepository challengeRepository;
    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final WorkRepository workRepository;
    private final PointRepository pointRepository;
    private final NotificationService notificationService;

    public boolean challengeResult(Challenge challenge, ChallengeParticipant participant) {
        switch (challenge.getType()) {
            case TOTAL:
                return isSuccessTotalChallenge(challenge, participant);
            case DAILY:
                return isSuccessDailyChallenge(challenge, participant);
            default:
                return false;
        }
    }

    private boolean isSuccessTotalChallenge(Challenge challenge, ChallengeParticipant participant) {
        int totalMinutes = workRepository.getPeriodTotalTimeByUser(
                participant.getUser().getId(),
                challenge.getStartDate(),
                challenge.getEndDate()
        );
        return totalMinutes >= challenge.getTargetMinutes();
    }

    private boolean isSuccessDailyChallenge(Challenge challenge, ChallengeParticipant participant) {
        int totalDays = calculateTotalDays(challenge);
        int successDays = countSuccessfulDays(challenge, participant);
        return successDays == totalDays;
    }

    private int calculateTotalDays(Challenge challenge) {
        return (int) ChronoUnit.DAYS.between(challenge.getStartDate(), challenge.getEndDate()) + 1;
    }

    private int countSuccessfulDays(Challenge challenge, ChallengeParticipant participant) {
        int successCount = 0;
        for (LocalDate date = challenge.getStartDate(); !date.isAfter(challenge.getEndDate()); date = date.plusDays(1)) {
            int dailyDuration = workRepository.getTodayDuration(participant.getUser(), date);
            if (dailyDuration >= challenge.getTargetMinutes()) {
                successCount++;
            }
        }
        return successCount;
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

                boolean isSuccess = challengeResult(ch, cp);

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
                if (isSuccess) {
                    notificationService.sendChallengeResultNotification(cp.getUser(), NotificationType.CHALLENGE,  "[" + ch.getTitle() + "]챌린지에 성공하셨습니다.",  succesPoint + "포인트가 지급되었습니다.");
                } else {
                    notificationService.sendChallengeResultNotification(cp.getUser(), NotificationType.CHALLENGE, "[" + ch.getTitle() + "]챌린지에 실패하셨습니다." ,  ch.getBetPoint() + "포인트가 차감되었습니다.");
                }
                Point point = Point.builder()
                        .point(succesPoint)
                        .user(cp.getUser())
                        .type(PointType.CHALLENGE_ADD)
                        .earnedAt(LocalDateTime.now())
                        .build();

                pointRepository.save(point);
            }

            for (ChallengeParticipant cp : failList) {

                cp.setSuccess(false);
                notificationService.sendChallengeResultNotification(cp.getUser(), NotificationType.CHALLENGE, "[" + ch.getTitle() + "]챌린지에 실패하셨습니다." ,  ch.getBetPoint() + "포인트가 차감되었습니다.");

              Point point = Point.builder()
                  .point(-ch.getBetPoint())
                  .user(cp.getUser())
                  .type(PointType.CHALLENGE_FAIL)
                  .earnedAt(LocalDateTime.now())
                  .build();

              pointRepository.save(point);
            }
        }
    }



}
