package fun.zhub.ppeng.feign.impl;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.feign.UserService;
import org.springframework.stereotype.Component;

/**
 * UserService 调用失败的兜底方法
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-06
 **/

@Component
public class UserFallbackServiceImpl implements UserService {
    @Override
    public ResponseResult<User> getUserInfo(Long userId) {
        return ResponseResult.fail("调用user服务失败");
    }
}
