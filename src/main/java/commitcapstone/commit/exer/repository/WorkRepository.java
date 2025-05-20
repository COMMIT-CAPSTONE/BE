package commitcapstone.commit.exer.repository;

import commitcapstone.commit.exer.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkRepository extends JpaRepository<Work, Long> {
    List<Long> findDurationByUserId(Long userId);
}
