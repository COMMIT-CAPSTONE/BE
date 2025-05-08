package commitcapstone.commit.oauth.repository;


import commitcapstone.commit.oauth.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
//    boolean existsByEmail(String email);

}
