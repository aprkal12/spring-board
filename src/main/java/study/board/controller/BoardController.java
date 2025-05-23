package study.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.board.entity.Board;
import study.board.entity.User;
import study.board.service.BoardService;

import java.io.IOException;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/")
    public String MainPage(){
        return "mainPage";
    }


    @GetMapping("/board/writeform")
    public String BoardWriteForm(){
        return "board/boardWriteForm";
    }

    @PostMapping("/board/write")
    public String BoardWrite(Board board, Model model, @RequestParam(name="file", required = false) MultipartFile file) throws IOException {
        boardService.write(board, file, model);
        return "message";
    }

    @PutMapping("/board/modify/{id}")
    public String BoardModify(@PathVariable("id") Integer id, Model model){
        boardService.boardView(id, model);
        return "board/boardModify";
    }

    @PutMapping("/board/update/{id}")
    public String BoardUpdate(@PathVariable("id") Integer id, Board board, Model model,
                              @RequestParam(name="file", required = false) MultipartFile file){
        boardService.boardModify(board, id, model, file);
        // 서비스를 호출하고 서비스에서 알람 처리하는게 맞는듯
        return "message";
    }

    @DeleteMapping("/board/delete/{id}")
    public String BoardDelete(@PathVariable("id") Integer id, Model model){
        boardService.boardDelete(id, model);
        return "message";
    }

    @GetMapping("/board/list")
    public String BoardList(Model model,
                            @PageableDefault(page=0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            @RequestParam(name="searchKeyword", defaultValue = "") String searchKeyword) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            boardService.boardList(model, pageable);
        }else{
            boardService.boardSearchList(model, searchKeyword, pageable);
        }
        return "board/boardList";
    }

//    @GetMapping("/board/search")
//    public String BoardListRedirect(RedirectAttributes redirectAttributes,
//                            @RequestParam(name="searchKeyword", defaultValue = "") String searchKeyword) {
//        redirectAttributes.addFlashAttribute("searchKeyword", searchKeyword);
//        return "redirect:boardList";
//    }

    @GetMapping("/board/view")
    public String BoardView(Model model,
                            @RequestParam("id") Integer id,
                            @RequestHeader(value = "referer", required = false) String referer){
        boardService.boardView(id, model);

        if (referer == null || !referer.startsWith("http://localhost:8080")) {
            referer = "/"; // 내부가 아니면 기본 페이지로, 주소는 배포 후에 바꿔야함
        }
        model.addAttribute("referer", referer);
        return "board/boardView";
    }
//        model.addAttribute("board", boardService.boardView(id));
//        return "board/boardView";
//    }

    @GetMapping("/user/{userid}/list")
    public String BoardUserList(@PathVariable("userid") String userid, Model model,
                                 @PageableDefault(page=0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        boardService.boardUserList(model, userid, pageable);
        return "board/boardUserList";
    }
}
