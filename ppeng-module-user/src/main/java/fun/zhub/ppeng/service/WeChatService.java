package fun.zhub.ppeng.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 微信服务调用
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-15
 **/

@FeignClient(url = "https://api.weixin.qq.com", name = "WeChatService")
@Component
public interface WeChatService {

    /**
     * 微信登录
     *
     * @param appid      小程序 appId
     * @param secret     小程序 appSecret
     * @param js_code    登录时获取的 code，可通过wx.login获取
     * @param grant_type 授权类型，此处只需填写 authorization_code
     * @return json：{"session_key":"h******+O7+ZaHyhBf9g==","openid":"oMoG25******Y*****"}
     */
    @GetMapping("/sns/jscode2session")
    String loginByWeChat(@RequestParam("appid") String appid, @RequestParam("secret") String secret, @RequestParam("js_code") String js_code, @RequestParam("grant_type") String grant_type);
}
