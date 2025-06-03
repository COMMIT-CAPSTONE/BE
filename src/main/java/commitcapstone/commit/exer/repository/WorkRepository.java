package commitcapstone.commit.exer.repository;

import commitcapstone.commit.exer.entity.Work;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {

    /*
    * 일일 운동 시간을 구하는 쿼리
    * @param : userId 사용자 아이디로 특정 사용자의 오늘 운동시간을 구함
    * @param : today 오늘 날짜로 서비스 코드에서 오늘 날짜를 구해 LocalDate타입으로 보내줌
    * return : 특정 사용자의 일일 운동 시간을 모두 더해 보내줌
    * */
    @Query("SELECT COALESCE(SUM(w.duration), 0) FROM Work w WHERE w.user.id = :userId AND w.workDate = :today")
    int getTodayDuration(@Param("userId") Long userId, @Param("today") LocalDate today);

    /*
     * 사용자의 총 운동 시간을 구하는 쿼리
     * @param : userId 사용자 아이디로 특정 사용자의 오늘 운동시간을 구함
     * return : 특정 사용자의 총 운동 시간을 모두 더해 보내줌
     * */
    @Query("SELECT COALESCE(SUM(w.duration), 0) FROM Work w WHERE w.user.id = :userId")
    int getTotalDuration(@Param("userId") Long userId);


    /*
     * 특정 기간 사이의 사용자의 운동 시간을 구하는 쿼리
     * @param : userId 사용자 아이디로 특정 사용자의 오늘 운동시간을 구함
     * @param : startDate 첫 날짜
     * @param : endDate 마지막 날짜
     * @return : 특정 기간 사이의 사용자의 운동 시간 총 합을 반환함
     * */
    @Query("SELECT COALESCE(SUM(w.duration), 0) FROM Work w WHERE w.user.id = :userId AND w.workDate BETWEEN :startDate AND :endDate")
    int getPeriodTotalTimeByUser(@Param("userId") Long userId,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);

    @Query(value = """
    SELECT AVG(user_total_duration)
    FROM (
        SELECT COALESCE(SUM(w.duration), 0) as user_total_duration
        FROM work w
        WHERE w.user_id != :userId
          AND w.work_date BETWEEN :startDate AND :endDate
        GROUP BY w.user_id
    ) AS user_totals
""", nativeQuery = true)
    Integer getPeriodToTalTimeBuOtherUsers(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}

