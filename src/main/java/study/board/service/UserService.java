package study.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import study.board.entity.User;
import study.board.repository.UserRepository;

import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void signUp(User user, Model model){
        try {
            UUID uuid = UUID.randomUUID();
            String username = uuid.toString();
            user.setUsername(username);
            userRepository.save(user);

            model.addAttribute("message", "회원가입이 완료되었습니다.");
        } catch (Exception e) {
            model.addAttribute("message", "회원가입에 실패했습니다.");
        } finally {
            model.addAttribute("searchUrl", "/board/list");
        }
    }
}
