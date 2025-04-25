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
import study.board.entity.User;
import study.board.repository.BoardRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserService userService;

    // 글 작성 처리
    public void write(Board board, MultipartFile file, Model model) throws IOException {
        try {
            // 파일이 비어있지 않은 경우에만 처리
            if (!file.isEmpty()) {
                String projectPath = "E:/spring/staticfiles";

                UUID uuid = UUID.randomUUID();
                String fileName = uuid + "_" + file.getOriginalFilename();

                File saveFile = new File(projectPath, fileName);
                file.transferTo(saveFile);

                board.setFilename(fileName);
                board.setFilepath("/files/" + fileName);
            } else {
                board.setFilename(null);
                board.setFilepath(null);
            }
            String username = userService.getCurrentUsername();
//            System.out.println("현재 사용자: " + username);

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
    public void boardView(Integer id, Model model) {
        Optional<Board> board = boardRepository.findById(id);

        if (board.isEmpty()) {
            Board boardisnull = new Board();
            boardisnull.setId(-1);
            boardisnull.setTitle("해당 게시글이 존재하지 않습니다.");
            model.addAttribute("board", boardisnull);
            return;
        }

        Board targetBoard = board.get();
        model.addAttribute("board", targetBoard);

        String filePath = targetBoard.getFilepath();
        if (filePath != null && !filePath.isBlank()) {
            Path path = Paths.get("E:/spring/staticfiles" + filePath.replace("/files/", "/"));
            try {
                String contentType = Files.probeContentType(path);
//                System.out.println("Content Type: " + contentType);
                if (contentType != null && contentType.startsWith("image")) {
                    model.addAttribute("fileType", "image");
                } else {
                    model.addAttribute("fileType", "file");
                }
            } catch (IOException e) {
                model.addAttribute("fileType", null); // 예외 발생 시 파일 없음 취급
            }
        } else {
            model.addAttribute("fileType", null); // 파일 없음
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

            // 파일이 새로 업로드되었는지 확인
            if (file != null && !file.isEmpty()) {
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                String projectPath = "E:/spring/staticfiles";
                File saveFile = new File(projectPath, fileName);
                file.transferTo(saveFile);

                boardtmp.setFilename(fileName);
                boardtmp.setFilepath("/files/" + fileName);
            }

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

        if(totalPages == 0){
            // 페이지가 없을 경우 페이징 관련 값을 설정하지 않음
            startPage = 1;
            endPage = 1;
            model.addAttribute("noPages", true);
        }else{
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
        }

        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("totalPages", totalPages);
    }
}
