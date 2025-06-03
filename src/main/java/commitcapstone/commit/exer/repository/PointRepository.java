package commitcapstone.commit.exer.repository;

import commitcapstone.commit.exer.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    @Query("SELECT COALESCE(SUM(p.point), 0) FROM Point p WHERE p.user.id = :userId")
    int findTotalPointByUserId(@Param("userId") Long userId);
}