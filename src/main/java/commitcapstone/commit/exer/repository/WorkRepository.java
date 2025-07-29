package commitcapstone.commit.exer.repository;

import commitcapstone.commit.user.User;
import commitcapstone.commit.exer.dto.response.ExerWeekStat;
import commitcapstone.commit.exer.entity.Work;


import commitcapstone.commit.rank.dto.BaseRankDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query("SELECT COALESCE(SUM(w.duration), 0) FROM Work w WHERE w.user = :user AND w.workDate = :today")
    Integer getTodayDuration(@Param("user") User user, @Param("today") LocalDate today);

    /*
     * 사용자의 총 운동 시간을 구하는 쿼리
     * @param : userId 사용자 아이디로 특정 사용자의 오늘 운동시간을 구함
     * return : 특정 사용자의 총 운동 시간을 모두 더해 보내줌
     * */
    @Query("SELECT COALESCE(SUM(w.duration), 0) FROM Work w WHERE w.user.id = :userId")
    Integer getTotalDuration(@Param("userId") Long userId);


    /*
     * 특정 기간 사이의 사용자의 운동 시간을 구하는 쿼리
     * @param : userId 사용자 아이디로 특정 사용자의 오늘 운동시간을 구함
     * @param : startDate 첫 날짜
     * @param : endDate 마지막 날짜
     * @return : 특정 기간 사이의 사용자의 운동 시간 총 합을 반환함
     * */
    @Query("SELECT COALESCE(SUM(w.duration), 0) FROM Work w WHERE w.user.id = :userId AND w.workDate BETWEEN :startDate AND :endDate")
    Integer getPeriodTotalTimeByUser(@Param("userId") Long userId,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate);

    @Query(value = """
                SELECT COALESCE(AVG(user_total_duration), 0)
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

    @Query("""
    SELECT new commitcapstone.commit.exer.dto.response.ExerWeekStat(w.workDate, SUM(w.duration)) 
    FROM Work w 
    WHERE w.user = :user AND w.workDate BETWEEN :start AND :end 
    GROUP BY w.workDate
""")
    List<ExerWeekStat> findDailyDurationsGrouped(
            @Param("user") User user,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("SELECT new commitcapstone.commit.rank.dto.BaseRankDto(u.name, u.tier, SUM(w.duration)) " +
            "FROM User u JOIN Work w ON u.id = w.user.id " +
            "WHERE w.workDate BETWEEN :start AND :end " +
            "GROUP BY u.name, u.tier " +
            "ORDER BY SUM(w.duration) DESC")
    Page<BaseRankDto> findExerTimeRankBetween(@Param("start") LocalDate start,
                                              @Param("end") LocalDate end,
                                              Pageable pageable);

    //평균 입장 시간 (분) = (운동 시간들의 총합) ÷ (입장 횟수)
    @Query("""
SELECT CASE WHEN COUNT(w) = 0 THEN 0 ELSE SUM(w.duration) * 1.0 / COUNT(w) END
FROM Work w
WHERE w.user.id = :userId
  AND w.workDate BETWEEN :startDate AND :endDate
""")
    Double getAverageWorkoutDurationByUser(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    //최대 입장 시간 (분) = MAX(운동 시간)
    @Query("""
SELECT COALESCE(MAX(w.duration), 0)
FROM Work w
WHERE w.user.id = :userId
  AND w.workDate BETWEEN :startDate AND :endDate
""")
    Integer getMaxWorkoutDurationByUser(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );



}

