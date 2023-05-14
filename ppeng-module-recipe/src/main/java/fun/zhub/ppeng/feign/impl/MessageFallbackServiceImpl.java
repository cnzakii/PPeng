package fun.zhub.ppeng.feign.impl;

import fun.zhub.ppeng.dto.AddUserMessageDTO;
import fun.zhub.ppeng.feign.MessageService;
import org.springframework.stereotype.Component;

/**
 * MessageService 调用失败的兜底方法
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-14
 **/
@Component
public class MessageFallbackServiceImpl implements MessageService {
    @Override
    public Boolean addMeaasge(AddUserMessageDTO messageDTO) {
        return false;
    }
}
