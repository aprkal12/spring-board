package study.board.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    // 양방향 연관관계: 이 유저가 작성한 게시글 목록
    @OneToMany(mappedBy = "author", orphanRemoval = true)
    private List<Board> boardList = new ArrayList<>();
}
