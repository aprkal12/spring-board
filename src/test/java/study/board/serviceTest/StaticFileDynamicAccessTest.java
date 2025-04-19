package study.board.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class StaticFileDynamicAccessTest {

    @Autowired
    private MockMvc mockMvc;

    private final String staticDirPath = "E:/spring/staticfiles/";
    private final String testFileName = "dynamic-test.txt";
    private final String testFileContent = "This is a test file.";

    @BeforeEach
    public void setup() throws IOException {
        // 디렉토리 생성 (없을 경우)
        Files.createDirectories(Paths.get(staticDirPath));

        // 테스트 파일 생성
        Files.write(Paths.get(staticDirPath + testFileName),
                testFileContent.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    @AfterEach
    public void cleanup() throws IOException {
        // 테스트 파일 삭제
        Files.deleteIfExists(Paths.get(staticDirPath + testFileName));
    }

    @Test
    public void 동적으로_생성된_파일_정적서빙_테스트() throws Exception {
        mockMvc.perform(get("/files/" + testFileName))
                .andExpect(status().isOk())
                .andExpect(content().string(testFileContent));
    }
}