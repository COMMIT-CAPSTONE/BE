package commitcapstone.commit.exer.repository;

import commitcapstone.commit.exer.entity.Point;
import commitcapstone.commit.rank.dto.BaseRankDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    @Query("SELECT COALESCE(SUM(p.point), 0) FROM Point p WHERE p.user.id = :userId")
    int findTotalPointByUserId(@Param("userId") Long userId);


    @Query("SELECT new commitcapstone.commit.rank.dto.BaseRankDto(u.name, u.tier, SUM(p.point)) " +
            "FROM User u JOIN Point p ON u.id = p.user.id " +
            "GROUP BY u.id, u.name, u.tier " +
            "ORDER BY SUM(p.point) DESC")
    Page<BaseRankDto> findTopUsersByTotalPoint(Pageable pageable);
}