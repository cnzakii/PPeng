package com.zhub.ppeng.constant;

/**
 * <p>
 * Sa-Token 常量
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-18
 **/
public interface SaTokenConstants {


    /**
     * 二级认证-更新用户密码
     */
    String SAFE_UPDATE_PASSWORD = "update:user:password";

    /**
     * 二级认证-更新用户邮箱
     */
    String SAFE_UPDATE_EMAIL = "update:user:password";

    /**
     * 二级认证-删除用户
     */
    String SAFE_DELETE_USER = "delete:user";

    /**
     * 二级认证过期时间-秒
     */
    Long SAFE_TIME = 300L;

    /**
     * 禁止登录
     */
    String DISABLE_LOGIN = "user:disable:login";
}
