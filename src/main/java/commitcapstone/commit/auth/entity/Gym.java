package commitcapstone.commit.auth.entity;

import jakarta.persistence.*;


@Entity
public class Gym {
    @Id @GeneratedValue
    private Long id;

    private String name;
    private String address;
    private double latitude;
    private double longitude;


    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}