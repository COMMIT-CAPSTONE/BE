package commitcapstone.commit.auth.entity;


import jakarta.persistence.*;
import lombok.Setter;


@Entity
@Setter
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "oauth_provider")
    private String oauthProvider;

    @Column(name = "oauth_id")
    private String oauthId;

    private String email;
    private String nickname;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Gym gym;
}