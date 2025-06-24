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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
//    boolean existsByOwnerAndFinishedFalse(User owner);

    Optional<Challenge> findById(Long id);

    List<Challenge> findAllByEndDateBeforeAndFinishedFalse(LocalDate date);

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

    //랭킹을 구하기
    @Query(
            value = "SELECT u.id, u.name, u.tier, COUNT(cp.id) AS success_count " +
                    "FROM user u " +
                    "JOIN challenge_participant cp ON u.id = cp.user_id " +
                    "WHERE cp.success = TRUE " +
                    "GROUP BY u.id, u.name, u.tier " +
                    "ORDER BY success_count DESC " +
                    "LIMIT 10",
            nativeQuery = true
    )
    List<Object[]> findTop10UsersBySuccessCountNative();




}
