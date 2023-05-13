package fun.zhub.ppeng.feign.interceptor;

import cn.dev33.satoken.same.SaSameUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

/**
 * OpenFeign拦截器, 在OpenFeign请求发出之前，加入一些操作
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-05-14
 **/
@Component
public class FeignInterceptor implements RequestInterceptor {

    /**
     * 在请求头中加入same-token
     *
     * @param requestTemplate requestTemplate
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(SaSameUtil.SAME_TOKEN, SaSameUtil.getToken());
    }
}
