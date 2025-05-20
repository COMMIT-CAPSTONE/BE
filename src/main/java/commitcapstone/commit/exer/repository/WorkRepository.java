package commitcapstone.commit.exer.repository;

import commitcapstone.commit.exer.entity.Work;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkRepository extends JpaRepository<Work, Long> {
    @Query("SELECT w.duration FROM Work w WHERE w.user.id = :userId")
    List<Long> findDurationByUserId(@Param("userId") Long userId);
}
