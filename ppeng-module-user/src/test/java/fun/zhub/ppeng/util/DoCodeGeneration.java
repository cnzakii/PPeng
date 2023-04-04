package fun.zhub.ppeng.util;


import cn.hutool.crypto.SmUtil;
import fun.zhub.ppeng.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
    public void getPassword() {

        String s = SmUtil.sm3("1637127723212214272123456");
        System.out.println(s);
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserService userService;

    @Test
    public void testRedis() {
        stringRedisTemplate.opsForValue().set("test", "1");
    }

    @Test
    public void testup() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/db_ppeng";//jdbc:mysql://localhost:3306/db1?useSSL
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ppeng","root","hsp");
        String sql = "update t_user set phone=123456 where is_deleted=0 and id=2 ";
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        statement.close();
        connection.close();
        System.out.println("成功");

    }


}
