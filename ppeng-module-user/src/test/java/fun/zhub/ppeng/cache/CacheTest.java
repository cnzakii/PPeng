package fun.zhub.ppeng.cache;

import fun.zhub.ppeng.PPengModuleUserApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;

/**
 * 测试本地缓存
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-08
 **/
@SpringBootTest(classes = PPengModuleUserApplication.class)
@Slf4j
public class CacheTest {

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void printCaffeineCacheDetails() {
        System.out.println(cacheManager.getClass().getName());
    }
}
