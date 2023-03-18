package fun.zhub.ppeng.util;


import cn.hutool.crypto.SmUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * <p>
 * 执行mybatis-plus代码生成器
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-17
 **/
@SpringBootTest
public class DoCodeGeneration {
//    @Test
//    public void generate() {
//
//        CodeGeneration.Generation(
//                "jdbc:mysql://47.115.207.226:3306/db_ppeng?useUnicode=true&characterEncoding=UTF-8&serverTimeZone=Asia/Shanghai",
//                "root",
//                "Ghidb!sd902#!.",
//
//                "t_user",
//                "t_user_info",
//                "t_like",
//                "t_follow"
//                );
//    }


    @Test
    public void getPassword(){

        String s = SmUtil.sm3("1637127723212214272123456");
        System.out.println(s);
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis(){
       stringRedisTemplate.opsForValue().set("test","1");
    }


}
