package study.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.board.entity.Board;
import study.board.repository.BoardRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    
    // 글 작성 처리
    public void write(Board board) {
        boardRepository.save(board);
    }
    
    // 게시글 리스트 처리
    public List<Board> boardlist() {
        return boardRepository.findAll();
    }

    // 특정 게시글 조회 처리
    public Board boardview(Integer id) {
        Optional<Board> board = boardRepository.findById(id);

        if (board.isPresent()) {
            return board.get();
        }else{
            Board boardisnull = new Board();
            boardisnull.setId(-1);
            boardisnull.setTitle("해당 게시글이 존재하지 않습니다.");
            return boardisnull;
        }
    }

    // 게시글 삭제 처리
    public void boarddelete(Integer id) {
        boardRepository.deleteById(id);
    }

    // 게시글 수정 처리
    public void boardedit(Board board) {
        boardRepository.save(board);
    }
}
