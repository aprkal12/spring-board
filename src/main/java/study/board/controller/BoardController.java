package study.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import study.board.entity.Board;
import study.board.service.BoardService;

import java.io.IOException;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;


    @GetMapping("/board/writeform")
    public String BoardWriteForm(){
        return "boardwriteform";
    }

    @PostMapping("/board/write")
    public String BoardWrite(Board board, Model model, @RequestParam(name="file", required = false) MultipartFile file) throws IOException {
        boardService.write(board, file, model);
        return "message";
    }

    @GetMapping("/board/list")
    public String BoardList(Model model, @PageableDefault(page=0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        Page<Board> list = boardService.boardlist(pageable);

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, list.getTotalPages());


        model.addAttribute("list", list);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("nowPage", nowPage);


        return "boardlist";
    }

    @GetMapping("/board/view")
    public String Boardview(Model model, @RequestParam("id") Integer id){
        model.addAttribute("board", boardService.boardview(id));
        return "boardview";
    }

    @PutMapping("/board/modify/{id}")
    public String BoardModify(@PathVariable("id") Integer id, Model model){
        model.addAttribute("board", boardService.boardview(id));
        return "boardmodify";
    }

    @PutMapping("/board/update/{id}")
    public String BoardUpdate(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file){
        boardService.boardmodify(board, id, model, file);
        // 서비스를 호출하고 서비스에서 알람 처리하는게 맞는듯
        return "message";
    }

    @DeleteMapping("/board/delete/{id}")
    public String BoardDelete(@PathVariable("id") Integer id, Model model){
        boardService.boarddelete(id, model);
        return "message";
    }
}
