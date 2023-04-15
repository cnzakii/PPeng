package fun.zhub.ppeng.handler;

import com.zhub.ppeng.common.ResponseResult;
import org.springframework.stereotype.Component;

/**
 * <p>
 * BadContentHandler 调用失败的兜底方法
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-15
 **/
@Component
public class BadContentFallbackHandler implements BadContentHandler {
    @Override
    public ResponseResult<String> handleBadNickName(Long id) {
        return ResponseResult.fail("调用失败");
    }
}
