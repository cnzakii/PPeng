package fun.zhub.ppeng.config;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.imageclassify.AipImageClassify;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
public class BaiduContentCensorConfigurer {

    @Value("${api.baidu.appId}")
    private String appId;

    @Value("${api.baidu.apiKey}")
    private String apiKey;

    @Value("${api.baidu.secretKey}")
    private String secretKey;


    /**
     * 配置内容审核client
     *
     * @return AipContentCensor
     */
    @Bean
    public AipContentCensor getAipContentCensor() {
        // 初始化一个AipContentCensor
        AipContentCensor client = new AipContentCensor(appId, apiKey, secretKey);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        return client;
    }


    /**
     * 配置图片识别client
     *
     * @return AipImageClassify
     */
    @Bean
    public AipImageClassify getAipImageClassify() {
        AipImageClassify client = new AipImageClassify(appId, apiKey, secretKey);
        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        return client;
    }
}
