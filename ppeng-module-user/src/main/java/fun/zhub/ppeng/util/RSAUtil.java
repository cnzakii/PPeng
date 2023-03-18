package fun.zhub.ppeng.util;

import cn.hutool.crypto.asymmetric.RSA;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 创建和维护单例RSA对象
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-18
 **/

@Component
public class RSAUtil {

    @Bean
    public RSA getRSA() {
        return new RSA();
    }
}
