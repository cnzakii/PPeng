package fun.zhub.ppeng.feign;


import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.feign.impl.UserFallbackServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 调用UserService服务
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-06
 **/
@Component
@FeignClient(value = "ppeng-module-user",fallback = UserFallbackServiceImpl.class)
public interface UserService {

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return success
     */
    @GetMapping("/user/info/{userId}")
    ResponseResult<User> getUserInfo(@PathVariable("userId") Long userId);

}
