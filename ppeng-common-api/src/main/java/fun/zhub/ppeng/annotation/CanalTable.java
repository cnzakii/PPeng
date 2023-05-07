package fun.zhub.ppeng.annotation;

import java.lang.annotation.*;

/**
 * Canal 监听器
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
