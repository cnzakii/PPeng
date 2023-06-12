package fun.zhub.ppeng;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 主启动类
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-17
 **/


@EnableDiscoveryClient
@SpringBootApplication
public class PPengModuleGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PPengModuleGatewayApplication.class);
    }
}
