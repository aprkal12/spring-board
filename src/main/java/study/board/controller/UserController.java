package study.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import study.board.entity.User;
import study.board.service.BoardService;
import study.board.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/signup")
    public String signupform() {
        return "user/signUp";
    }

    @PostMapping("/user/signup")
    public String signup(User user, Model model) {
        userService.signUp(user, model);

        return "message";
    }
}
