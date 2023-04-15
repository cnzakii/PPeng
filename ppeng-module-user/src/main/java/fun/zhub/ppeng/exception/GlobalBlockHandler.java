package fun.zhub.ppeng.exception;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.zhub.ppeng.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 全局Block处理器
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-04-15
 **/
@Slf4j
@Component
public class GlobalBlockHandler {

    /**
     * 通用管控规则处理类
     *
     * @param exception BlockException
     * @return fail
     */
    public static ResponseResult<String> handleCommonBlockException(BlockException exception) {
        String msg = exception.getLocalizedMessage();
        log.info(msg);
        return ResponseResult.fail(msg);
    }
}
