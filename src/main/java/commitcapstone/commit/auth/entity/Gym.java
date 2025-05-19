package commitcapstone.commit.auth.entity;

import jakarta.persistence.*;
import lombok.Setter;


@Entity
@Setter
public class Gym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private double latitude;
    private double longitude;


    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}