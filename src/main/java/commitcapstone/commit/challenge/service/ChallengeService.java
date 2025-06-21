package commitcapstone.commit.challenge.service;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.auth.repository.UserRepository;
import commitcapstone.commit.challenge.dto.*;
import commitcapstone.commit.challenge.entity.Challenge;
import commitcapstone.commit.challenge.entity.ChallengeParticipant;
import commitcapstone.commit.challenge.entity.ChallengeSortType;
import commitcapstone.commit.challenge.entity.ChallengeType;
import commitcapstone.commit.challenge.repository.ChallengeParticipantRepository;
import commitcapstone.commit.challenge.repository.ChallengeRepository;
import commitcapstone.commit.challenge.utils.Utils;
import commitcapstone.commit.common.code.ChallengeErrorCode;
import commitcapstone.commit.common.exception.ChallengeException;
import commitcapstone.commit.exer.entity.Point;
import commitcapstone.commit.exer.entity.PointType;
import commitcapstone.commit.exer.repository.PointRepository;
import commitcapstone.commit.exer.repository.WorkRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChallengeService {
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final PointRepository pointRepository;
    private final ChallengeParticipantRepository challengeParticipantRepository;
    private final WorkRepository workRepository;
    private final Utils utils;


    public ChallengeService(UserRepository userRepository, ChallengeRepository challengeRepository, PointRepository pointRepository, ChallengeParticipantRepository challengeParticipantRepository, WorkRepository workRepository, Utils utils) {
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.pointRepository = pointRepository;
        this.challengeParticipantRepository = challengeParticipantRepository;
        this.workRepository = workRepository;
        this.utils = utils;
    }

        @Transactional
        public ChallengeCreateResponse saveChallenge(String email, ChallengeCreateRequest request) {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));

            int userPoint = pointRepository.findTotalPointByUserId(user.getId());

            System.out.println("=== saveChallenge 호출 ===");

            System.out.println("Request ChallengeTitle: " + request.getChallengeTitle());
            System.out.println("Request StartDate: " + request.getStartDate());
            System.out.println("Request EndDate: " + request.getEndDate());
            System.out.println("Request BetPoint: " + request.getBetPoint());
            System.out.println("Request TargetMinutes: " + request.getTargetMinutes());
            Period period = Period.between(request.getStartDate(), request.getEndDate());
            int beetWeenDays = period.getDays() + 1;  // inclusive 계산

            //사용자 베팅 포인트가 음수일 때
            if (userPoint < 0) {
                throw new ChallengeException(ChallengeErrorCode.INVALID_USER_POINT);
            }

            //유저 포인트 < 베팅 포인트인 경우 에러 처리
            if (userPoint < request.getBetPoint()) {
                throw new ChallengeException(ChallengeErrorCode.INSUFFICIENT_USER_POINTS);
            }

            // dailay는 1시간 이상 ~ 24시간
            //total 최소 : 일수 X 1시간 최대 일수 x 24
            if(request.getChallengeType().equals(ChallengeType.DAILY)) {
                if (request.getTargetMinutes() < 60 || request.getTargetMinutes() > 1440) {
                    throw new ChallengeException(ChallengeErrorCode.INVALID_DAILY_GOAL_TIME);
                }
            } else if (request.getChallengeType().equals(ChallengeType.TOTAL)) {
                if (request.getTargetMinutes() < 60 * beetWeenDays || request.getTargetMinutes() > 1440 * beetWeenDays) {
                    throw new ChallengeException(ChallengeErrorCode.INVALID_TOTAL_GOAL_TIME);
                }
            }

            //이미 사용자가 참여중인 챌린지가 있는지 확인
            /*todo: challengeRepository.existsByOwner(user)가 단순히 존재 여부가 아니라 현재 진행 중인 챌린지만 체크하는지 확인 필요 - 완료
            */
            if (challengeParticipantRepository.existsByUserAndFinishedFalse(user)) {
                throw new ChallengeException(ChallengeErrorCode.USER_HAVE_ONLY_ONE_CHALLENGE);
            }

            // 시작일이 종료일보다 나중일 때 처리
            if (request.getStartDate().isAfter(request.getEndDate())) {
                throw new ChallengeException(ChallengeErrorCode.INVALID_DATE_RANGE);
            }

            if (beetWeenDays < 3) {
                throw new ChallengeException(ChallengeErrorCode.INVALID_CHALLENGE_PERIOD);
            }


            LocalDateTime now = LocalDateTime.now();
            Challenge challenge = Challenge.builder()
                    .owner(user)
                    .title(request.getChallengeTitle())
                    .description(request.getChallengeDescription())
                    .type(request.getChallengeType())
                    .betPoint(request.getBetPoint())
                    .targetMinutes(request.getTargetMinutes())
                    .startDate(request.getStartDate())
                    .endDate(request.getEndDate())
                    .finished(false)
                    .createdAt(now).build();

            challengeRepository.save(challenge);



            Point point = Point.builder()
                    .user(user)
                    .point(request.getBetPoint())
                    .type(PointType.CHALLENGE_MINUS)
                    .earnedAt(now)
                    .build();
            pointRepository.save(point);

            ChallengeParticipant challengeParticipant = ChallengeParticipant.builder()
                    .user(user)
                    .challenge(challenge)
                    .finished(false)
                    .build();

            challengeParticipantRepository.save(challengeParticipant);








            return ChallengeCreateResponse.builder()
                    .challengeTitle(challenge.getTitle())
                    .challengeDescription(challenge.getDescription())
                    .challengeType(challenge.getType())
                    .betPoint(challenge.getBetPoint())
                    .targetMinutes(challenge.getTargetMinutes())
                    .startDate(challenge.getStartDate())
                    .endDate(challenge.getEndDate())
                    .build();
        }

    public Page<ChallengeListResponse> getChallenges(ChallengeType type, String keyword, int page, int size, ChallengeSortType sortType) {

        Sort sort = utils.getSort(sortType);
        Pageable pageable = PageRequest.of(page, size, sort);


        if (keyword == null || keyword.trim().isEmpty()) {
            if (type.equals(ChallengeType.ALL)) {
                return challengeRepository.findAll(pageable)
                        .map(ChallengeListResponse::from);
            } else {
                return challengeRepository.findByType(pageable, type)
                        .map(ChallengeListResponse::from);
            }
        } else {
            if (type.equals(ChallengeType.ALL)) {
                return challengeRepository.findByTitle(keyword, pageable)
                        .map(ChallengeListResponse::from);
            } else {
                return challengeRepository.findByTypeAndTitle(type, keyword, pageable)
                        .map(ChallengeListResponse::from);
            }
        }
    }

    public ChallengeDetailResponse getChallengeDetail(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다." + email));

        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 챌린지가 존재하지 않습니다. id=" + id));


        int partiicipants = challengeParticipantRepository.countByChallenge(challenge);
        ChallengeDetailResponse response = ChallengeDetailResponse.from(challenge, user, partiicipants);
        boolean isJoined = challengeParticipantRepository.existsByUserAndChallengeAndFinishedFalse(user, challenge);

        if (isJoined) {
            return challengeJoinUserDetail(response, user, challenge);
        }
        return response;

    }

    public ChallengeDetailResponse challengeJoinUserDetail(ChallengeDetailResponse response, User user, Challenge challenge) {
        List<ChallengeMyProgress> progressList = new ArrayList<>();
        LocalDate start = challenge.getStartDate();
        LocalDate end = challenge.getEndDate();
        LocalDate today = LocalDate.now();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            int achieve = workRepository.getTodayDuration(user, date);
            boolean isToday = date.equals(today);

            ChallengeMyProgress progress;

            if (challenge.getType() == ChallengeType.DAILY) {
                progress = ChallengeMyProgress.dailyFrom(date, challenge.getTargetMinutes(), achieve, isToday);
            } else if (challenge.getType() == ChallengeType.TOTAL) {
                progress = ChallengeMyProgress.totalForm(date, challenge.getTargetMinutes(), achieve, isToday);
            } else {
                // 혹시 future type 생길 경우 방어 코드
                throw new IllegalStateException("Unknown ChallengeType: " + challenge.getType());
            }

            progressList.add(progress);
        }

        response.setMyProgressList(progressList);
        return response;
    }


    public ChallengeJoinResponse joinChallenge(long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다." + email));


        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 챌린지가 존재하지 않습니다. id=" + id));

        if (challengeParticipantRepository.existsByUserAndChallengeAndFinishedFalse(user, challenge)) {
            throw new ChallengeException(ChallengeErrorCode.USER_HAVE_ONLY_ONE_CHALLENGE);
        }


        LocalDate today = LocalDate.now();
        if (!today.isBefore(challenge.getStartDate())) {
            throw new ChallengeException(ChallengeErrorCode.ALREAY_START_CHALLENGE);
        }

        ChallengeParticipant participant = ChallengeParticipant.builder()
                .user(user)
                .challenge(challenge)
                .finished(false)
                .build();

        challengeParticipantRepository.save(participant);

        LocalDateTime now = LocalDateTime.now();
        Point point = Point.builder()
                .user(user)
                .point(challenge.getBetPoint())
                .type(PointType.CHALLENGE_MINUS)
                .earnedAt(now)
                .build();
        pointRepository.save(point);


        return ChallengeJoinResponse.builder().id(challenge.getId()).build();
    }



}
