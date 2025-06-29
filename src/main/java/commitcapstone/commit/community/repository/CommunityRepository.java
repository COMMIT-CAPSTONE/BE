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

    @Query(value = """
    SELECT c.*, 
           COALESCE(r.count, 0) AS reaction_count, 
           COALESCE(cm.count, 0) AS comment_count
    FROM community c
    LEFT JOIN (
        SELECT target_id, COUNT(*) AS count
        FROM reaction
        GROUP BY target_id
    ) r ON c.id = r.target_id
    LEFT JOIN (
        SELECT community_id, COUNT(*) AS count
        FROM comment
        GROUP BY community_id
    ) cm ON c.id = cm.community_id
    WHERE c.is_deleted = false
    ORDER BY (COALESCE(r.count, 0) + COALESCE(cm.count, 0)) DESC
    LIMIT 10
    """, nativeQuery = true)
    List<Community> findTop10Popular();

}