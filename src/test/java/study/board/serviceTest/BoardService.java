package study.board.serviceTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import study.board.entity.Board;
import study.board.entity.User;
import study.board.repository.BoardRepository;
import study.board.repository.UserRepository;
import study.board.service.BoardService;
import study.board.service.UserService;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("local")
class BoardServiceTest {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    // 테스트 후 파일 정리용
    @AfterEach
    void cleanUpUploadedFiles() {
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
        File directory = new File(path);
        if (directory.exists()) {
            for (File file : directory.listFiles()) {
                file.delete();
            }
        }
    }

    @WithMockUser(username = "testuser")
    @Test
    void 게시글_작성_테스트() throws Exception {
        // given
        Board board = new Board();
        board.setTitle("Test Title");
        board.setContent("Test Content");

        File testImage = new File("src/test/resources/test-image.png");
        System.out.println("파일 절대 경로: " + testImage.getAbsolutePath());
        FileInputStream fis = new FileInputStream(testImage);
        MockMultipartFile file = new MockMultipartFile("file", testImage.getName(), "image/png", fis);

        Model model = new ConcurrentModel();

        User testuser = new User();
        testuser.setUserid("testuser");
        testuser.setPassword("testpassword");

        userRepository.save(testuser);

        // when
        boardService.write(board, file, model);

        // then
        List<Board> list = boardRepository.findAll();
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getTitle()).isEqualTo("Test Title");
        assertThat(list.get(0).getFilename()).contains("test-image");
        assertThat(model.getAttribute("message")).isEqualTo("게시글이 작성되었습니다.");
    }

    @Test
    void 게시글_조회_테스트() {
        // given
        Board board = new Board();
        board.setTitle("조회 제목");
        board.setContent("조회 내용");
        boardRepository.save(board);

        Model model = new ConcurrentModel();
        // when
        boardService.boardView(board.getId(), model);

        Optional<Board> board1 = boardRepository.findById(board.getId());


        // then
        assertThat(board1.get().getTitle()).isEqualTo("조회 제목");
    }

    @Test
    void 게시글_삭제_테스트() {
        // given
        Board board = new Board();
        board.setTitle("삭제 테스트");
        board.setContent("삭제 내용");
        boardRepository.save(board);
        Model model = new ConcurrentModel();

        // when
        boardService.boardDelete(board.getId(), model);

        // then
        assertThat(boardRepository.findById(board.getId())).isEmpty();
        assertThat(model.getAttribute("message")).isEqualTo("게시글이 삭제되었습니다.");
    }

    @Test
    void 게시글_수정_테스트() {
        // given
        Board board = new Board();
        board.setTitle("수정 전");
        board.setContent("내용 전");
        boardRepository.save(board);

        Board newBoard = new Board();
        newBoard.setTitle("수정 후");
        newBoard.setContent("내용 후");

        Model model = new ConcurrentModel();

        // when
        boardService.boardModify(newBoard, board.getId(), model, null);

        // then
        Board updated = boardRepository.findById(board.getId()).get();
        assertThat(updated.getTitle()).isEqualTo("수정 후");
        assertThat(updated.getContent()).isEqualTo("내용 후");
        assertThat(model.getAttribute("message")).isEqualTo("게시글이 수정되었습니다.");
    }
}
