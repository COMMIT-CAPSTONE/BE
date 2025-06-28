package commitcapstone.commit.community.repository;

import commitcapstone.commit.community.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    //그 커뮤니티의 댓글 총 갯수
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.community.id = :communityId AND c.isDeleted = false")
    int countByCommunityId(@Param("communityId") Long communityId);

    List<Comment> findAllByCommunityIdOrderByCreatedAtAsc(Long communityId);
}
