package fun.zhub.ppeng.service.impl;

import fun.zhub.ppeng.service.WeChatService;
import org.springframework.stereotype.Component;

/**
 * <p>
 * WeChatService 服务调用失败处理类
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-17
 **/
@Component
public class WeChatFallbackServiceImpl implements WeChatService {
    @Override
    public String loginByWeChat(String appid, String secret, String js_code, String grant_type) {
        return null;
    }
}
