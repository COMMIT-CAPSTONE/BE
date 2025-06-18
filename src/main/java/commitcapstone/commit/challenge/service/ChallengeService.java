package commitcapstone.commit.challenge.service;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.auth.repository.UserRepository;
import commitcapstone.commit.challenge.dto.ChallengeDetailResponse;
import commitcapstone.commit.challenge.dto.ChallengeListResponse;
import commitcapstone.commit.challenge.dto.ChallengeCreateRequest;
import commitcapstone.commit.challenge.dto.ChallengeCreateResponse;
import commitcapstone.commit.challenge.entity.Challenge;
import commitcapstone.commit.challenge.entity.ChallengeSortType;
import commitcapstone.commit.challenge.entity.ChallengeType;
import commitcapstone.commit.challenge.repository.ChallengeRepository;
import commitcapstone.commit.challenge.utils.Utils;
import commitcapstone.commit.common.code.ChallengeErrorCode;
import commitcapstone.commit.common.exception.ChallengeException;
import commitcapstone.commit.exer.entity.Point;
import commitcapstone.commit.exer.entity.PointType;
import commitcapstone.commit.exer.repository.PointRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class ChallengeService {
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final PointRepository pointRepository;
    private final Utils utils;


    public ChallengeService(UserRepository userRepository, ChallengeRepository challengeRepository, PointRepository pointRepository, Utils utils) {
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.pointRepository = pointRepository;
        this.utils = utils;
    }

    @Transactional
    public ChallengeCreateResponse saveChallenge(String email, ChallengeCreateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));

        int userPoint = pointRepository.findTotalPointByUserId(user.getId());
        long beetWeenDays = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;

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
        if (challengeRepository.existsByOwnerAndIsFinishedFalse(user)) {
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
                .isFinished(false)
                .createdAt(now).build();

        challengeRepository.save(challenge);



        Point point = Point.builder()
                .user(user)
                .point(request.getBetPoint())
                .type(PointType.CHALLENGE_MINUS)
                .earnedAt(now)
                .build();
        pointRepository.save(point);



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

    public ChallengeDetailResponse getChallengeDetail(Long id) {
        Challenge challenge = challengeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 챌린지가 존재하지 않습니다. id=" + id));

        User user = userRepository.findById(challenge.getOwner().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 챌린지의 유저가 존재하지 않습니다."));

        return ChallengeDetailResponse.from(challenge, user);
    }





}
