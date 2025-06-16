package commitcapstone.commit.challenge.service;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.auth.repository.UserRepository;
import commitcapstone.commit.challenge.dto.GetChallengesResponse;
import commitcapstone.commit.challenge.dto.PostChallengeRequest;
import commitcapstone.commit.challenge.dto.PostChallengeResponse;
import commitcapstone.commit.challenge.entity.Challenge;
import commitcapstone.commit.challenge.entity.ChallengeSortType;
import commitcapstone.commit.challenge.entity.ChallengeType;
import commitcapstone.commit.challenge.repository.ChallengeRepository;
import commitcapstone.commit.challenge.utils.Utils;
import commitcapstone.commit.common.code.ChallengeErrorCode;
import commitcapstone.commit.common.exception.ChallengeException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ChallengeService {
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;
    private final Utils utils;


    public ChallengeService(UserRepository userRepository, ChallengeRepository challengeRepository, Utils utils) {
        this.userRepository = userRepository;
        this.challengeRepository = challengeRepository;
        this.utils = utils;
    }

    public PostChallengeResponse saveChallenge(String email, PostChallengeRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));

        if (challengeRepository.existsByOwner(user)) {
            throw new ChallengeException(ChallengeErrorCode.USER_HAVE_ONLY_ONE_CHALLENGE);
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

        return PostChallengeResponse.builder()
                .challengeTitle(challenge.getTitle())
                .challengeDescription(challenge.getDescription())
                .challengeType(challenge.getType())
                .betPoint(challenge.getBetPoint())
                .targetMinutes(challenge.getTargetMinutes())
                .startDate(challenge.getStartDate())
                .endDate(challenge.getEndDate())
                .build();
    }

    public Page<GetChallengesResponse> getChallenges(ChallengeType type, String keyword, int page, int size, ChallengeSortType sortType) {

        Sort sort = utils.getSort(sortType);
        Pageable pageable = PageRequest.of(page, size, sort);


        if (keyword == null || keyword.trim().isEmpty()) {
            if (type.equals(ChallengeType.ALL)) {
                return challengeRepository.findAll(pageable)
                        .map(GetChallengesResponse::from);
            } else {
                return challengeRepository.findByType(pageable, type)
                        .map(GetChallengesResponse::from);
            }
        } else {
            if (type.equals(ChallengeType.ALL)) {
                return challengeRepository.findByTitle(keyword, pageable)
                        .map(GetChallengesResponse::from);
            } else {
                return challengeRepository.findByTypeAndTitle(type, keyword, pageable)
                        .map(GetChallengesResponse::from);
            }
        }



    }



}
