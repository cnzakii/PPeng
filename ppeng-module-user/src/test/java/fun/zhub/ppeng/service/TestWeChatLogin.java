package fun.zhub.ppeng.service;

import fun.zhub.ppeng.PPengModuleUserApplication;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static fun.zhub.ppeng.contants.WeChatApiContants.*;

/**
 * <p>
 * 测试微信功能
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-15
 **/
@SpringBootTest(classes = PPengModuleUserApplication.class)
@Slf4j
public class TestWeChatLogin {

    @Resource
    private WeChatService weChatService;


    @Test
    void TestLogin() {
        String s = weChatService.loginByWeChat(APP_ID, SECRET, "0e3UXMFa1jvG8F0VIxHa1CWKfu4UXMFu", GRANT_TYPE);
        log.info(s);
        /*
         * {"session_key":"h******+O7+ZaHyhBf9g==","openid":"oMoG25dS828qlRqRY*****"}
         */
    }

}
