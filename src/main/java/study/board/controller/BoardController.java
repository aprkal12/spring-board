package study.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import study.board.entity.Board;
import study.board.service.BoardService;

@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;


    @GetMapping("/board/writeform")
    public String BoardWriteForm(){
        return "boardwriteform";
    }

    @PostMapping("/board/write")
    public String BoardWrite(Board board){
        boardService.write(board);
        return "";
    }

    @GetMapping("/board/list")
    public String BoardList(Model model){
        model.addAttribute("list", boardService.boardlist());
        return "boardlist";
    }

    @GetMapping("/board/view")
    public String Boardview(Model model, @RequestParam("id") Integer id){
        model.addAttribute("board", boardService.boardview(id));
        return "boardview";
    }

    @DeleteMapping("/board/delete/{id}")
    public String BoardDelete(@PathVariable("id") Integer id){
        boardService.boarddelete(id);
        return "redirect:/board/list";
    }
}
