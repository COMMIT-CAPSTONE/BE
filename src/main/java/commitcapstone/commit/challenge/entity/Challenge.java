package commitcapstone.commit.challenge.entity;

import commitcapstone.commit.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //* 중요
@AllArgsConstructor //* 중요
@Builder
@Table(name = "Challenges")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false, length = 20)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChallengeType type;

    @Column(name = "bet_point", nullable = false)
    private int betPoint;

    @Column(name = "target_minutes", nullable = false)
    private int targetMinutes;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "is_finished", nullable = false)
    private boolean isFinished = false;

}
