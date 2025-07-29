package commitcapstone.commit.exer.entity;

import commitcapstone.commit.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //* 중요
@AllArgsConstructor //* 중요
@Builder
@Table(name = "point")
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer point;

    @Enumerated(EnumType.STRING)
    private PointType type;

    @Column(name = "earned_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime earnedAt;
}