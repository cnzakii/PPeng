package fun.zhub.ppeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <p>
 * 主启动类
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-17
 **/
@SpringBootApplication
@EnableDiscoveryClient
public class PPengModuleSearch {
    public static void main(String[] args) {
        SpringApplication.run(PPengModuleSearch.class);
    }
}
