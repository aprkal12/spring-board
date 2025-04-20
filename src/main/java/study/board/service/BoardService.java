package study.board.service;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import study.board.entity.Board;
import study.board.repository.BoardRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    
    // 글 작성 처리
    public void write(Board board, MultipartFile file, Model model) throws IOException {
        try {
            String projectPath = "E:/spring/staticfiles";

            UUID uuid = UUID.randomUUID();
            String fileName = uuid + "_" + file.getOriginalFilename();

            File saveFile = new File(projectPath, fileName);

            file.transferTo(saveFile);

            board.setFilename(fileName);
            board.setFilepath("/files/" + fileName);

            boardRepository.save(board);

            model.addAttribute("message", "게시글이 작성되었습니다.");
        }catch (Exception e){
            model.addAttribute("message", "게시글 작성에 실패했습니다.");
        } finally {
            model.addAttribute("searchUrl", "/board/list");
        }
    }
    
    // 게시글 리스트 처리
    public void boardList(Model model, Pageable pageable) {
        Page<Board> list = boardRepository.findAll(pageable);
        paging(model, list);
    }
    
    // 게시글 검색 처리
    public void boardSearchList(Model model, String searchKeyword, Pageable pageable) {
        Page<Board> list = boardRepository.findByTitleContaining(searchKeyword, pageable);
        paging(model, list);
    }

    // 특정 게시글 조회 처리
    public Board boardView(Integer id) {
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
    public void boardDelete(Integer id, Model model) {
        try {
            boardRepository.deleteById(id);
            model.addAttribute("message", "게시글이 삭제되었습니다.");
        }catch (Exception e) {
            model.addAttribute("message", "게시글 삭제에 실패했습니다.");
        } finally {
            model.addAttribute("searchUrl", "/board/list");
        }
    }

    // 변경 감지를 이용한 수정 메서드
    @Transactional // 변경 감지 기능을 사용하여 수정된 필드만 업데이트
    public void boardModify(Board board, Integer id, Model model, MultipartFile file) {
        try {
            Board boardtmp = boardRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

            boardtmp.setTitle(board.getTitle());
            boardtmp.setContent(board.getContent());
            // save 호출 없이 트랜잭션 커밋 시점에 자동으로 UPDATE 됨

            model.addAttribute("message", "게시글이 수정되었습니다.");
        } catch (Exception e) {
            model.addAttribute("message", "게시글 수정에 실패했습니다.");
        } finally {
            model.addAttribute("searchUrl", "/board/list");
        }
    }

    // 페이징 처리 메서드
    private void paging(Model model, Page<Board> list){
        int totalPages = list.getTotalPages();
        int pagecount = 5;

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = 0;
        int endPage = 0;

        // 기본 중앙 정렬 계산
        int halfBlock = pagecount / 2;
        startPage = nowPage - halfBlock;
        endPage = nowPage + halfBlock;

        // 왼쪽 끝 보정
        if (startPage < 1) {
            startPage = 1;
            endPage = Math.min(pagecount, totalPages);
        }

        // 오른쪽 끝 보정
        if (endPage > totalPages) {
            endPage = totalPages;
            startPage = Math.max(1, endPage - pagecount + 1);
        }

        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("totalPages", totalPages);
    }
}
