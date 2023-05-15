package fun.zhub.ppeng;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 测试redis
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-15
 **/
@SpringBootTest(classes = PPengModuleRecipeApplication.class)
public class RedisTest {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testZSet() {
        String key = "testZest";

        String value = "测试数据";
        // 插入数据
        stringRedisTemplate.opsForZSet().addIfAbsent(key, value, 2);

        // 获取数据
        Double score = stringRedisTemplate.opsForZSet().score(key, value);

        System.out.println(score);

        // 插入数据
        stringRedisTemplate.opsForZSet().addIfAbsent(key, "测试数据", 2888888);

        // 获取数据
        score = stringRedisTemplate.opsForZSet().score(key, value);

        System.out.println(score);

    }


}
