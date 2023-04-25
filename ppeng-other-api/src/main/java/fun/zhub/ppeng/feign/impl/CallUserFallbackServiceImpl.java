package fun.zhub.ppeng.feign.impl;

import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.entity.User;
import fun.zhub.ppeng.feign.CallUserService;
import org.springframework.stereotype.Component;

/**
 * <p>
 * CallUserService 调用失败的兜底方法
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-15
 **/
@Component
public class CallUserFallbackServiceImpl implements CallUserService {
    @Override
    public ResponseResult<String> handleBadNickName(Long id) {
        return ResponseResult.fail("调用user模块失败");
    }

    @Override
    public ResponseResult<User> getUserInfo(Long userId) {
        return ResponseResult.fail("调用user模块失败");
    }
}
