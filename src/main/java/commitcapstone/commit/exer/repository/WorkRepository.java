package commitcapstone.commit.exer.repository;

import commitcapstone.commit.exer.dto.response.PeriodDurationResponse;
import commitcapstone.commit.exer.entity.Work;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
    @Query(value = """
        SELECT SUM(w.duration)
        FROM Work w
        WHERE w.user.id = :userId
    """)
    Integer findDurationByUserId(@Param("userId") Long userId);

    @Query(value = """
        SELECT SUM(w.duration)
        FROM Work w
        WHERE w.user.id = :userId  AND w.workDate = :date
    """)
    Integer getTotalDurationByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);


    @Query(value = """
        SELECT AVG(t.total_duration)
        FROM (
            SELECT SUM(w.duration) AS total_duration
            FROM work w
            WHERE w.work_date = :date AND w.user_id != :userId
            GROUP BY w.user_id
        ) AS t
    """, nativeQuery = true)
    Double getAverageOtherUserDuration(@Param("date") LocalDate date, @Param("userId") Long userId);




        @Query(value = """
            SELECT w.work_date AS date, SUM(w.duration) AS totalDuration
            FROM work w
            WHERE w.user_id = :userId
              AND w.work_date BETWEEN :startDate AND :endDate
            GROUP BY w.work_date
            ORDER BY w.work_date
        """, nativeQuery = true)
        List<PeriodDurationResponse> findDailyDurationByUserAndDateRange(
                @Param("userId") Long userId,
                @Param("startDate") LocalDate startDate,
                @Param("endDate") LocalDate endDate);


}

