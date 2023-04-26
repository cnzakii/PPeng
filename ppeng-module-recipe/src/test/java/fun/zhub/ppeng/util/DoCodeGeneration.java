package fun.zhub.ppeng.util;

import com.zhub.ppeng.util.CodeGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * <p>
 * 执行Mybatis-plus代码生成器
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-17
 **/
@SpringBootTest
public class DoCodeGeneration {
    @Test
    public void generate() {

        CodeGeneration.Generation(
                "jdbc:mysql://47.115.207.226:3306/db_ppeng?useUnicode=true&characterEncoding=UTF-8&serverTimeZone=Asia/Shanghai",
                "root",
                "Ghidbsd902df",

                "t_recipe_censor"
        );
    }

}