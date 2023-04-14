package fun.zhub.ppeng.config;

import com.baidu.aip.contentcensor.AipContentCensor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static fun.zhub.ppeng.constants.BaiduApiConstants.*;

/**
 * <p>
 * 百度内容审核配置类
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-14
 **/

@Configuration
public class BaiduContentCensorConfig {
    @Bean
    public AipContentCensor getAipContentCensor(){
        // 初始化一个AipContentCensor
        AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        return client;
    }
}
