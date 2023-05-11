package fun.zhub.ppeng.service;

import fun.zhub.ppeng.PPengOtherApiApplication;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 图片识别测试
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-11
 **/
@SpringBootTest(classes = PPengOtherApiApplication.class)
@Slf4j
public class TestImageRecognition {

    @Resource
    private ImageRecognitionService recognitionService;

    @Test
    void testDishRecognition() {
        String filePath = "/Users/zaki/Desktop/test.jpeg";


        try {
            Path path = Paths.get(filePath);
            String fileName = path.getFileName().toString();
            String contentType = Files.probeContentType(path);
            byte[] fileBytes = Files.readAllBytes(path);
            MultipartFile file = new MockMultipartFile(fileName, fileName, contentType, fileBytes);

            var map = recognitionService.recognizeDish(file);

            map.forEach((s, aDouble) -> System.out.println(s + ": " + aDouble));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

