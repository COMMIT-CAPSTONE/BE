package commitcapstone.commit.oauth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "oauth_provider", nullable = false, length = 50)
    private String oauthProvider; // oauth제공자 - google, kakao, naver

    @Column(name = "oauth_id", nullable = false, unique = true, length = 100)
    private String oauthId; // 사용자의 해당 소셜 로그인 고유 아이디

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 50)
    private String nickname; // 사용자 닉네임

    @Column(name = "profile_image_url", columnDefinition = "TEXT")
    private String profileImageUrl; // 프로필 이미지 URL

    @Column(nullable = false)
    private Integer exp = 0; // 경험치

    @Column(nullable = false)
    private Integer point = 0; // 포인트

    @Column(nullable = false, length = 30)
    private String tier = "bronze"; // 티어

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "gym_id", foreignKey = @ForeignKey(name = "fk_user_gym"))
//    private Gym gym; // 헬스장 정보

}
