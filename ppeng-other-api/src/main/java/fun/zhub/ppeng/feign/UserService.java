package fun.zhub.ppeng.feign;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.feign.impl.UserFallbackServiceImpl;
import fun.zhub.ppeng.feign.interceptor.FeignInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * <p>
 * 利用OpenFeign调用user模块 处理内容违规
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-14
 **/
@FeignClient(value = "ppeng-module-user", fallback = UserFallbackServiceImpl.class,configuration = FeignInterceptor.class)
@Component
public interface UserService {

    /**
     * 处理昵称违规
     *
     * @param id 用户id
     * @return success
     */
    @PostMapping("/handle/user/nick/name/{id}")
    ResponseResult<String> handleBadNickName(@PathVariable("id") Long id);


    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return success
     */
    @GetMapping("/user/info/{userId}")
    ResponseResult<User> getUserInfo(@PathVariable("userId") Long userId);

}
