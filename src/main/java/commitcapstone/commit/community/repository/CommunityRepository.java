package commitcapstone.commit.community.repository;

import commitcapstone.commit.community.entity.Community;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    Page<Community> findByTitleContainingIgnoreCaseOrBodyContainingIgnoreCase(String keyword, String keyword2, Pageable pageable);
    Page<Community> findAll(Pageable pageable);


}