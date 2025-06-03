package commitcapstone.commit.exer.repository;

import commitcapstone.commit.exer.entity.Point;
import commitcapstone.commit.exer.entity.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

}