package fun.zhub.ppeng.exception;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.zhub.ppeng.common.ResponseResult;
import com.zhub.ppeng.common.ResponseStatus;
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
     * @param e BlockException
     * @return fail
     */
    public static ResponseResult<String> handleCommonBlockException(BlockException e) {
        String msg = null;
        if (e instanceof FlowException) {
            msg = "Request Flow Restriction";
        } else if (e instanceof ParamFlowException) {
            msg = "Hotspot Request Restriction";
        } else if (e instanceof DegradeException) {
            msg = "Request Circuit Breaker";
        } else if (e instanceof AuthorityException) {
            return ResponseResult.base(ResponseStatus.HTTP_STATUS_401);
        }
        return ResponseResult.base(ResponseStatus.HTTP_STATUS_500,null,msg);
    }
}
