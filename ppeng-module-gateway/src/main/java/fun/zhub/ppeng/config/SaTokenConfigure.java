package fun.zhub.ppeng.config;


import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import fun.zhub.ppeng.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static fun.zhub.ppeng.common.ResponseStatus.HTTP_STATUS_401;
import static fun.zhub.ppeng.constant.RoleConstants.ROLE_ADMIN;
import static fun.zhub.ppeng.constant.RoleConstants.ROLE_USER;
import static fun.zhub.ppeng.constant.SaTokenConstants.*;

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
                .addInclude("/**")
                // 开放地址
                .addExclude(
                        "/user/login/**",
                        "/user/rsa",
                        "/mail/register/**",
                        "/user/register/**"
                )
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    /*
                     * 登录校验
                     */
                    SaRouter.match("/**").check(r -> StpUtil.checkLogin());

                    /*
                     * 禁止登录检查
                     */
                    SaRouter.match("/**").check(r -> StpUtil.checkDisable(StpUtil.getLoginId(), DISABLE_LOGIN));

                    /*
                     * 角色认证
                     */
                    // 需要user角色的接口
                    SaRouter.match(
                            "/user/**",
                            "/mail/update/**",
                            "/file/upload/**"
                    ).notMatch(
                            "/user/info/**"
                    ).check(r -> StpUtil.checkRole(ROLE_USER));


                    // 需要admin权限
                    SaRouter.match(
                            "/user/info/**"
                    ).check(r -> StpUtil.checkRole(ROLE_ADMIN));


                    /*
                     *用户二次认证
                     */
                    // 更新用户密码
                    SaRouter.match("/user/update/password").check(r -> StpUtil.checkSafe(SAFE_UPDATE_PASSWORD));
                    // 更新用户邮箱
                    SaRouter.match("/user/update/email").check(r -> StpUtil.checkSafe(SAFE_UPDATE_EMAIL));
                    // 删除用户
                    SaRouter.match("/user/current").match(SaHttpMethod.DELETE).check(r -> StpUtil.checkSafe(SAFE_DELETE_USER));


                })
                // 异常处理方法：每次setAuth函数出现异常时进入
                .setError(e -> {
                            log.warn(e.getMessage());

                            if (e instanceof NotPermissionException) {
                                return JSONUtil.toJsonStr(ResponseResult.base(HTTP_STATUS_401, null, "无权限"));
                            } else {
                                return JSONUtil.toJsonStr(ResponseResult.base(HTTP_STATUS_401, null, e.getLocalizedMessage()));
                            }


                        }
                );
    }
}
