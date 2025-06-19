package commitcapstone.commit.challenge.repository;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.challenge.entity.ChallengeParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeParticipantRepository extends JpaRepository<ChallengeParticipant, Long> {
    boolean existsByUserAndIsFinishedFalse(User user);

}
