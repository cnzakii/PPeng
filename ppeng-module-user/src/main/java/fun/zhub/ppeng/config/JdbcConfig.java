package fun.zhub.ppeng.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * <p>
 * JDBC配置类
 * <p>
 *
 * @author lbl
 * @version 1.0
 * @since 2023.4.2
 **/
@Configuration
public class JdbcConfig {
    @Value("${ppeng.datasource.username}")
    private String username;
    @Value("${ppeng.datasource.password}")
    private String password;
    @Value("${ppeng.datasource.url}")
    private String url;

    @Value("${ppeng.datasource.driver-class-name}")
    private String driver;

    @Bean
    public DataSource getDataSource() {
        DruidDataSource ds = new DruidDataSource();
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setUrl(url);
        ds.setDriverClassName(driver);
        return ds;
    }
}
