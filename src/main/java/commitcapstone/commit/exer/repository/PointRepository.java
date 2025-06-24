package commitcapstone.commit.exer.repository;

import commitcapstone.commit.exer.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    @Query("SELECT COALESCE(SUM(p.point), 0) FROM Point p WHERE p.user.id = :userId")
    int findTotalPointByUserId(@Param("userId") Long userId);

    @Query(
            value = "SELECT u.id, u.name, u.tier, SUM(p.amount) AS total_point " +
                    "FROM user u " +
                    "JOIN point p ON u.id = p.user_id " +
                    "GROUP BY u.id, u.name, u.tier " +
                    "ORDER BY total_point DESC " +
                    "LIMIT 10",
            nativeQuery = true
    )
    List<Object[]> findTop10UsersByTotalPoint();
}