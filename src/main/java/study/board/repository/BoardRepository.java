package study.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.board.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Page<Board> findByTitleContaining(String searchKeyword, Pageable pageable);
    Page<Board> findByAuthorUserid(String userid, Pageable pageable);
    Page<Board> findByAuthorUsername(String username, Pageable pageable);
}
