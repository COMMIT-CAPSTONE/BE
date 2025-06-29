package commitcapstone.commit.challenge.repository;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.challenge.entity.Challenge;
import commitcapstone.commit.challenge.entity.ChallengeType;
import commitcapstone.commit.rank.dto.BaseRankDto;
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
    @Query("SELECT new commitcapstone.commit.rank.dto.BaseRankDto(u.name, u.tier, COUNT(cp.id)) " +
            "FROM User u JOIN ChallengeParticipant cp ON u.id = cp.user.id " +
            "WHERE cp.success = TRUE " +
            "GROUP BY u.name, u.tier " +
            "ORDER BY COUNT(cp.id) DESC")
    Page<BaseRankDto> findTopUsersBySuccessCount(Pageable pageable);


    
    }
