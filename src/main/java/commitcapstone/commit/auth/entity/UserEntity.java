package commitcapstone.commit.auth.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "oauth_provider")
    private String oauthProvider;

    @Column(name = "oauth_id")
    private String oauthId;

    @Column(name = "password")
    private String password;

    @Column(name = "points", nullable = false)
    private Long points;

    @Column(name = "profile_img", nullable = false)
    private String profileImg;

    @Column(name = "gender", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "experience", nullable = false)
    private Long experience;

    @Column(name = "tier", nullable = false)
    @Enumerated(EnumType.STRING)
    private Tier tier;
}


