package study.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import study.board.entity.User;
import study.board.service.BoardService;
import study.board.service.UserService;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signupform() {
        return "user/signUp";
    }

    @PostMapping("/signup")
    public String signup(User user, Model model) {
        userService.signUp(user, model);

        return "message";
    }

    @GetMapping("/user/login")
    public String loginForm() {
        return "user/login";
    }

    @GetMapping("/mypage")
    public String getmypage(Authentication authentication, Model model) {
        String name = authentication.getName();
        return "redirect:/user/" + name + "/mypage";
    }

    @GetMapping("/user/{userid}/mypage")
    public String myPage(@PathVariable String userid, Model model) {
        userService.myPage(userid, model);
        return "user/myPage";
    }

    @PostMapping("/user/{userid}/email")
    public String setEmail(@PathVariable String userid, @RequestParam String email, Model model) {
        userService.setEmail(userid, email, model);
        return "message";
    }

    @PutMapping("/user/{userid}/email")
    public String updateEmail(@PathVariable String userid, @RequestParam String email, Model model) {
        userService.updateEmail(userid, email, model);
        return "message";
    }

    @PutMapping("/user/{userid}/password")
    public String updatePassword(@PathVariable String userid, @RequestParam String password, Model model) {
        userService.updatePassword(userid, password, model);
        return "message";
    }

}
