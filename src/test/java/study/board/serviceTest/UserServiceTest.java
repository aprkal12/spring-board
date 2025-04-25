package study.board.serviceTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import study.board.service.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("local")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void 로그인된_사용자_이름을_가져온다() {
        // when
        String username = userService.getCurrentUsername();
        System.out.println("로그인된 사용자 이름: " + username);

        // then
        assertEquals("testuser", username);
    }

}
