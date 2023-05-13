package fun.zhub.ppeng.config;

import cn.dev33.satoken.same.SaSameUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Same-Token，定时刷新
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-14
 **/
@Configuration
public class SaSameTokenRefreshTask {
    // 从 0 分钟开始 每隔 10 分钟执行一次 Same-Token
    @Scheduled(cron = "0 0/10 * * * ? ")
    public void refreshToken() {
        SaSameUtil.refreshToken();
    }
}
