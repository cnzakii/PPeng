package fun.zhub.ppeng.feign.impl;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.feign.UserService;
import org.springframework.stereotype.Component;

/**
 * <p>
 * UserService 调用失败的兜底方法
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-15
 **/
@Component
public class UserFallbackServiceImpl implements UserService {


    @Override
    public ResponseResult<String> handleBadNickName(Long id, String msg) {
        return ResponseResult.fail("调用user模块失败");
    }

    @Override
    public ResponseResult<String> handleBadIcon(Long id, String msg) {
        return ResponseResult.fail("调用user模块失败");
    }

    @Override
    public ResponseResult<User> getUserInfo(Long userId) {
        return ResponseResult.fail("调用user模块失败");
    }
}
