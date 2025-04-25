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

    @Column(length = 50, nullable = false, unique = true)
    private String userid;

    @Column(length = 500, nullable = false)
    private String password;

    @Column(length = 50)
    private String username;

    @Column(length = 50)
    private String email;
}
