package commitcapstone.commit.notification;

import java.util.List;
import commitcapstone.commit.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByIsReadAscCreatedAtAsc(@Param("userId") Long userId);
}
