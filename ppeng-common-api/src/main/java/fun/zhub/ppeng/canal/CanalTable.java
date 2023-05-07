package fun.zhub.ppeng.canal;

import java.lang.annotation.*;

/**
 * CanalTable注解<br>
 * 使用该注解的类将 用于处理特定表的变化数据<br>
 * 例如：带有@CanalTable("user") 并继承AbstractCanalHandler<T>的类将用于处理user表中添加，修改，删除数据后的操作后的操作
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-06
 **/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CanalTable {

    /**
     * 表名
     */
    String value() default "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";

}
