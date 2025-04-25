package study.board.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Data
@DynamicUpdate
@Table(name = "board") // 실제 테이블명이 board.board이지만 스키마는 생략 가능
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(length = 150)
    private String filename;

    @Column(length = 300)
    private String filepath;

    // 외래키 userinfo.userid를 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", referencedColumnName = "userid") // 명시적으로 어떤 컬럼을 참조할지 지정
    private User author;
}
