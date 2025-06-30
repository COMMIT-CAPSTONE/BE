package commitcapstone.commit.community.repository;

import commitcapstone.commit.community.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Page<Community> findAll(Pageable pageable);

    Page<Community> findByAuthorId(Long authorId, Pageable pageable);

    @Query("SELECT c FROM Community c WHERE c.isDeleted = false ORDER BY (c.reactionCount + c.commentCount) DESC")
    List<Community> findTop10PopularPosts(Pageable pageable);

    Page<Community> findByTitleContainingOrContentContaining(String keyword1, String keyword2, Pageable pageable);
}

