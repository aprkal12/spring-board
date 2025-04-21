package study.board.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "userinfo")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true, nullable = false)
    private String userid;
    @Column(nullable = false)
    private String password;
    private String username;
    private String email;

}
