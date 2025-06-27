package commitcapstone.commit.community.repository;

import commitcapstone.commit.community.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    // 그 게시글의 총 리액션 수
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.targetPost.id = :communityId")
    int countByCommunityId(@Param("communityId") Long communityId);

    // 그 게시글의 리액션 타입별 개수
    @Query("SELECT r.reactionType, COUNT(r) FROM Reaction r WHERE r.targetPost.id = :communityId GROUP BY r.reactionType")
    List<Object[]> countGroupedByType(@Param("communityId") Long communityId);
}
