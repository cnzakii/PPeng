package fun.zhub.ppeng.config;

import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.fromValue;

/**
 * 自定义限流异常返回结果
 *
 * @author Zaki
 * @version 2.0
 * @since 2023-05-24
 **/

@Slf4j
@Configuration
public class MyBlockRequestHandler implements BlockRequestHandler {
    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {

        String msg = null;
        if (throwable instanceof FlowException) {
            msg = "Too Many Requests: Flow Restriction";
        } else if (throwable instanceof ParamFlowException) {
            msg = "Too Many Requests: Hotspot Restriction";
        } else if (throwable instanceof DegradeException) {
            msg = "Too Many Requests: Circuit Breaker";
        } else if (throwable instanceof AuthorityException) {
            return ServerResponse.status(200)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromValue(ResponseResult.base(ResponseStatus.HTTP_STATUS_401)));
        }

        // 返回自定义限流异常返回结果
        return ServerResponse.status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromValue(ResponseResult.base(ResponseStatus.HTTP_STATUS_429, msg)));
    }
}
