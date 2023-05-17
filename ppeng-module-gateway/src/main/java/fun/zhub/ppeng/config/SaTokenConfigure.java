package fun.zhub.ppeng.config;


import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.zhub.ppeng.common.ResponseResult;
import fun.zhub.ppeng.common.ResponseStatus;
import fun.zhub.ppeng.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static fun.zhub.ppeng.constant.RoleConstants.ROLE_ADMIN;
import static fun.zhub.ppeng.constant.RoleConstants.ROLE_USER;
import static fun.zhub.ppeng.constant.SaTokenConstants.*;

/**
 * 注册 Sa-Token全局过滤器
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
                        "/user/register/**",
                        "/recipe/comment/list/**",
                        "/recipe/type/list",
                        "/recipe/list/**",
                        "/recipe/recommend/**"
                )
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    /*
                     * 登录校验
                     */
                    SaRouter.match("/**").check(r -> StpUtil.checkLogin());


                    /*
                     * 角色认证
                     */
                    // 需要user角色的接口
                    SaRouter.match(
                            "/user/**",
                            "/mail/update/**",
                            "/file/upload/**",
                            "/message/**",
                            "/recipe/**",
                            "/image/recognition/**"
                    ).check(r -> StpUtil.checkRole(ROLE_USER));


                    // 需要admin权限
                    SaRouter.match(
                            "/hidden/admin/**"
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
                            log.info(e.getLocalizedMessage());
                            ObjectMapper objectMapper = new ObjectMapper();
                            String json;
                            try {
                                json = objectMapper.writeValueAsString(ResponseResult.base(ResponseStatus.HTTP_STATUS_401, e.getLocalizedMessage()));
                            } catch (JsonProcessingException ex) {
                                log.error("Failed to convert object to JSON string===>{}", ex.getLocalizedMessage());
                                throw new BusinessException(ResponseStatus.HTTP_STATUS_500, "Failed to convert object to JSON string");
                            }
                            return json;
                        }
                );
    }
}
