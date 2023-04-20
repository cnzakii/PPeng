package fun.zhub.ppeng.config;

import cn.dev33.satoken.reactor.filter.SaReactorFilter;
import cn.dev33.satoken.router.SaHttpMethod;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONUtil;
import com.zhub.ppeng.common.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.zhub.ppeng.common.ResponseStatus.HTTP_STATUS_401;
import static com.zhub.ppeng.constant.RoleConstants.ROLE_ADMIN;
import static com.zhub.ppeng.constant.RoleConstants.ROLE_USER;
import static com.zhub.ppeng.constant.SaTokenConstants.*;

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
                .addExclude("/user/login/**", "/user/rsa", "/mail/register/**", "/user/register/**")
                // 鉴权方法：每次访问进入
                .setAuth(obj -> {
                    /*
                     * 登录校验
                     */
                    SaRouter.match("/**").check(r -> StpUtil.checkLogin());

                    /*
                     * 角色认证
                     */
                    // User模块:包含UserController，UserInfoController,FollowController,LikeController
                    SaRouter.match("/user/**").check(r -> StpUtil.checkRole(ROLE_USER));

                    // User模块，用于模块间的调用,外部访问需要admin权限
                    SaRouter.match("/user/info/**").check(r -> StpUtil.checkRole(ROLE_ADMIN));

                    // mail模块,发送修改个人信息邮件验证码
                    SaRouter.match("/mail/update/**").check(r -> StpUtil.checkRole(ROLE_USER));

                    // file模块,上传文件
                    SaRouter.match("/file/upload/**").check(r -> StpUtil.checkRole(ROLE_USER));


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
                            return JSONUtil.toJsonStr(ResponseResult.base(HTTP_STATUS_401, null, e.getMessage()));
                        }
                );
    }
}
