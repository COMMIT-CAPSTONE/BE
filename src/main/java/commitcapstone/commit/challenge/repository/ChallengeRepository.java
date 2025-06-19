package commitcapstone.commit.challenge.repository;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.challenge.entity.Challenge;
import commitcapstone.commit.challenge.entity.ChallengeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
//    boolean existsByOwnerAndIsFinishedFalse(User owner);

    Optional<Challenge> findById(Long id);

    //타입만 선택
    Page<Challenge> findByType(Pageable pageable, ChallengeType type);

    //검색어 입력 , 타입은 ALL
    @Query("SELECT c FROM Challenge c WHERE c.title LIKE %:keyword%")
    Page<Challenge> findByTitle(
                                       @Param("keyword") String keyword,
                                       Pageable pageable);

    //검색어 입력, 타입 선택
    @Query("SELECT c FROM Challenge c WHERE c.title LIKE %:keyword% AND c.type = :type")
    Page<Challenge> findByTypeAndTitle(@Param("type") ChallengeType type,
                                       @Param("keyword") String keyword,
                                       Pageable pageable);
}
