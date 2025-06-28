package commitcapstone.commit.community.repository;

import commitcapstone.commit.community.entity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    // 그 게시글의 총 리액션 수
    @Query("SELECT COUNT(r) FROM Reaction r WHERE r.targetPost.id = :communityId")
    int countByCommunityId(@Param("communityId") Long communityId);

    // 그 게시글의 리액션 타입별 개수
    @Query("SELECT r.reactionType, COUNT(r) FROM Reaction r WHERE r.targetPost.id = :communityId GROUP BY r.reactionType")
    List<Object[]> countGroupedByType(@Param("communityId") Long communityId);

    // 사용자가 해당 게시글에 남긴 리액션 조회
    @Query("SELECT r FROM Reaction r WHERE r.targetPost.id = :communityId AND r.user.id = :userId")
    Optional<Reaction> findByCommunityIdAndUserId(@Param("communityId") Long communityId, @Param("userId") Long userId);
}

