package fun.zhub.ppeng;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * PPengModuleMessageApplication
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-13
 **/

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("fun.zhub.ppeng.mapper")
public class PPengModuleMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(PPengModuleMessageApplication.class);
    }
}
