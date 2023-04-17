package fun.zhub.ppeng.service.impl;

import com.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.service.BadContentService;
import org.springframework.stereotype.Component;

/**
 * <p>
 * BadContentService 调用失败的兜底方法
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-15
 **/
@Component
public class BadContentFallbackServiceImpl implements BadContentService {
    @Override
    public ResponseResult<String> handleBadNickName(Long id) {
        return ResponseResult.fail("调用失败");
    }
}
