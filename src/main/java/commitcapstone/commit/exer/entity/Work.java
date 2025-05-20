package commitcapstone.commit.exer.entity;

import commitcapstone.commit.auth.entity.User;
import jakarta.persistence.*;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Setter
public class Work {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long duration;

    @Column(name = "work_date")
    private LocalDate workDate;


    @ManyToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;


}
