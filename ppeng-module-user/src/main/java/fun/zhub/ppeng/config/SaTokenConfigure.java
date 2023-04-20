package fun.zhub.ppeng.config;

import cn.dev33.satoken.strategy.SaStrategy;
import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 自定义token
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-20
 **/
@Configuration
public class SaTokenConfigure {

    /**
     * 重写 Sa-Token 框架内部算法策略
     */
    @Autowired
    public void rewriteSaStrategy() {
        // 重写 Token 生成策略
        SaStrategy.me.createToken = (loginId, loginType) -> "p_" + RandomUtil.randomString(14) + "_" + RandomUtil.randomString(14) + "_";
    }
}
