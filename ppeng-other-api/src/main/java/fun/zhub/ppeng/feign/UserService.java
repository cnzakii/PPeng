package fun.zhub.ppeng.feign;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.feign.impl.UserFallbackServiceImpl;
import fun.zhub.ppeng.feign.interceptor.FeignInterceptor;
import jakarta.websocket.server.PathParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 利用OpenFeign调用user模块 处理内容违规
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-14
 **/
@FeignClient(value = "ppeng-module-user", fallback = UserFallbackServiceImpl.class, configuration = FeignInterceptor.class)
@Component
public interface UserService {


    /**
     * 处理昵称违规
     *
     * @param id  用户消息
     * @param msg 违规信息
     * @return success
     */
    @PostMapping("/user/censor/nick/name")
    ResponseResult<String> handleBadNickName(@PathParam("id") Long id, @RequestParam("msg") String msg);


    /**
     * 处理头像违规
     *
     * @param id  用户消息
     * @param msg 违规信息
     * @return success
     */
    @PostMapping("/icon")
    ResponseResult<String> handleBadIcon(@RequestParam("id") Long id, @RequestParam("msg") String msg);

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return success
     */
    @GetMapping("/user/info/{userId}")
    ResponseResult<User> getUserInfo(@PathVariable("userId") Long userId);

}
