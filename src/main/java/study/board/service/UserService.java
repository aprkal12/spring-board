package study.board.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {
        User user = userRepository.findByUserid(userid)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

//        System.out.println("아이디: " + user.getUserid());
//        System.out.println("비밀번호: " + user.getPassword());

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

            String username = user.getUserid();
            user.setUsername(username);
            userRepository.save(user);

            model.addAttribute("message", "회원가입이 완료되었습니다.");
        } catch (Exception e) {
            model.addAttribute("message", "회원가입에 실패했습니다.");
        } finally {
            model.addAttribute("searchUrl", "/board/list");
        }
    }
    public void myPage(String userid, Model model) {
        Optional<User> user = userRepository.findByUserid(userid);
        if(user.isPresent()) {
            User currentUser = user.get();
            model.addAttribute("user", currentUser);
        } else {
            model.addAttribute("user", null);
        }
    }

    @Transactional
    public void setEmail(String userid, String email, Model model){
        try {
            Optional<User> user = userRepository.findByUserid(userid);
            if(user.isPresent()) {
                User currentUser = user.get();
                currentUser.setEmail(email);
//                userRepository.save(currentUser);
                model.addAttribute("message", "이메일이 등록되었습니다.");
            } else {
                model.addAttribute("message", "사용자를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            model.addAttribute("message", "이메일 등록에 실패했습니다.");
        } finally {
            model.addAttribute("searchUrl", "/user/" + userid + "/mypage");
        }
    }

    @Transactional
    public void updateEmail(String userid, String email, Model model) {
        try {
            Optional<User> user = userRepository.findByUserid(userid);
            if (user.isPresent()) {
                User currentUser = user.get();
                currentUser.setEmail(email);
                model.addAttribute("message", "이메일이 수정되었습니다.");
            }else{
                model.addAttribute("message", "사용자를 찾을 수 없습니다.");
            }
        }catch (Exception e) {
            model.addAttribute("message", "이메일 수정에 실패했습니다.");
        } finally {
            model.addAttribute("searchUrl", "/user/" + userid + "/mypage");
        }
    }

    @Transactional
    public void updatePassword(String userid, String password, Model model) {
        try {
            Optional<User> user = userRepository.findByUserid(userid);
            if (user.isPresent()) {
                User currentUser = user.get();
                String encryptedPassword = bCryptPasswordEncoder.encode(password);
                currentUser.setPassword(encryptedPassword);
                model.addAttribute("message", "비밀번호가 수정되었습니다.");
            }else{
                model.addAttribute("message", "사용자를 찾을 수 없습니다.");
            }
        }catch (Exception e) {
            model.addAttribute("message", "비밀번호 수정에 실패했습니다.");
        } finally {
            model.addAttribute("searchUrl", "/user/" + userid + "/mypage");
        }
    }

    public String getCurrentUserid() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

//        System.out.println("로그인된 사용자: " + authentication.getPrincipal());
        return authentication.getName(); // principal이 username 문자열일 경우
    }



}
