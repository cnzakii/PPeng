package fun.zhub.ppeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p>
 *  主启动类
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-17
 **/

@EnableDiscoveryClient
@SpringBootApplication
@EnableFeignClients

public class PPengOtherApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(PPengOtherApiApplication.class);
    }
}
