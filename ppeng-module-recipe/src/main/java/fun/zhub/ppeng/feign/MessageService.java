package fun.zhub.ppeng.feign;

import fun.zhub.ppeng.dto.AddUserMessageDTO;
import fun.zhub.ppeng.feign.impl.MessageFallbackServiceImpl;
import fun.zhub.ppeng.feign.interceptor.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 利用OpenFeign调用message模块
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-14
 **/
@FeignClient(value = "ppeng-module-message", fallback = MessageFallbackServiceImpl.class, configuration = FeignInterceptor.class)
@Component
public interface MessageService {

    @PostMapping("/message/add")
    Boolean addMeaasge(@RequestBody AddUserMessageDTO messageDTO);
}
