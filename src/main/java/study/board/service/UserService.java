package study.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import study.board.entity.User;
import study.board.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // spring security 로그인 구현체
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username이 userid임
        User user = userRepository.findByUserid(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        System.out.println("아이디: " + user.getUserid());
        System.out.println("비밀번호: " + user.getPassword());

        return new org.springframework.security.core.userdetails.User(
                user.getUserid(), // 아이디
                user.getPassword(), // 암호화된 비번
                List.of() // 권한이 없을 경우 빈 리스트
        );
    }

    public void signUp(User user, Model model){
        try {
            // 비밀번호 암호화
            String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

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
