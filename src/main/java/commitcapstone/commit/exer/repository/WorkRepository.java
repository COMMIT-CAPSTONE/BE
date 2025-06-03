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

    @Query("SELECT COALESCE(SUM(w.duration), 0) FROM Work w WHERE w.user.id = :userId AND w.workDate = :today")
    int getTodayDuration(@Param("userId") Long userId, @Param("today") LocalDate today);


    @Query("SELECT COALESCE(SUM(w.duration), 0) FROM Work w WHERE w.user.id = :userId")
    int getTotalDuration(@Param("userId") Long userId);

}

