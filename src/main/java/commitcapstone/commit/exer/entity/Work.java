package commitcapstone.commit.exer.entity;

import commitcapstone.commit.auth.entity.User;
import commitcapstone.commit.challenge.entity.Challenge;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //* 중요
@AllArgsConstructor //* 중요
@Builder
@Setter
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "BIGINT")
    private Long duration;

    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenge challenge;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}