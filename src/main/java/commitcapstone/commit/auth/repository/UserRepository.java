package commitcapstone.commit.auth.repository;


import commitcapstone.commit.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByOauthProviderAndEmail(String provider, String email);

}
