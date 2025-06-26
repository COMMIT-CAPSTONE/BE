package commitcapstone.commit.auth.repository;


import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.tier.TierType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByOauthProviderAndEmail(String provider, String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);

    @Query("SELECT u.tier FROM User u WHERE u.id = :user")
    Optional<TierType> findTierByUserId(@Param("user") User user);

    boolean existsByName(String name);
}
