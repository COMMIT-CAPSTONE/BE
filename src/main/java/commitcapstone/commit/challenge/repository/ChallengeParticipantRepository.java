package commitcapstone.commit.challenge.repository;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.challenge.entity.Challenge;
import commitcapstone.commit.challenge.entity.ChallengeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {
    boolean existsByUserAndFinishedFalse(User user);
    boolean existsByUserAndChallengeAndFinishedFalse(User user, Challenge challenge);

    int countByChallenge(Challenge challenge);
    List<ChallengeParticipant> findAllByChallenge(Challenge challenge);

}
