package fun.zhub.ppeng.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.zhub.ppeng.common.ResponseResult;
import static com.zhub.ppeng.common.ResponseStatus.HTTP_STATUS_400;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 注册 Sa-Token全局过滤器
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-18
 **/
@Configuration
@Slf4j
public class SaTokenConfigure {

    @Bean
    public SaReactorFilter getSaReactorFilter() {
        return new SaReactorFilter()
                // 拦截地址
                .addInclude("/**")    // 拦截全部path
                // 开放地址
                .addExclude("/user/login/**")
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    // 登录校验 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
                    SaRouter.match("/**", r -> StpUtil.checkLogin());

                    // 权限认证 -- 不同模块, 校验不同权限
                    SaRouter.match("/user/**", r -> StpUtil.checkRole("user"));
                    SaRouter.match("/admin/**", r -> StpUtil.checkRole("admin"));


                    // 更多匹配 ...  */
                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> {
                    log.error(e.getMessage());

                    return JSONUtil.toJsonStr(ResponseResult.base(HTTP_STATUS_400, null,e.getMessage()));
                })
                ;
    }
}
