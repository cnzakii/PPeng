package fun.zhub.ppeng.config;

import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.same.SaSameUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 全局拦截配置类<br>
 * 仅用于服务间调用时的权限认证<br>
 * 目的：防止用户调用不对外暴露的接口
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-14
 **/
@Configuration
@Slf4j
public class MyWebMvcConfigurer implements WebMvcConfigurer {

    /**
     * 配置服务间调用时的权限验证
     *
     * @return SaServletFilter
     */
    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                .addInclude("/handler/recipe/**")
                .setAuth(obj -> {
                    // 校验 Same-Token 身份凭证     Same-Token为服务间调用独有标识
                    SaSameUtil.checkCurrentRequestToken();
                })
                .setError(e -> {
                    log.info(e.getLocalizedMessage());
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json;
                    try {
                        json = objectMapper.writeValueAsString(ResponseResult.base(ResponseStatus.HTTP_STATUS_401));
                    } catch (JsonProcessingException ex) {
                        log.error("Failed to convert object to JSON string===>{}", ex.getLocalizedMessage());
                        throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "Failed to convert object to JSON string");
                    }
                    return json;
                });
    }
}
