package com.zhub.ppeng.constant;

/**
 * <p>
 * 和Redis相关的常量，包括但不限于Key的前缀，过期时间
 * <p>
 *
 * @author Zaki
 * @version 1.0
 * @since 2023-03-18
 **/
public interface RedisConstants {

    /**
     * 手机验证码前缀
     */
    String LOGIN_CODE_KEY = "login:code:";

    /**
     * 手机验证码过期时间
     */
    Long LOGIN_CODE_TTL = 2L;

    /**
     * 用户角色前缀
     */
    String USER_ROLE = "user:role:";

    /**
     * 用户角色信息过期时间
     */
    Long USER_ROLE_TTL = 30L;


    /**
     * 用户关注数据前缀
     */
    String USER_FOLLOWS = "user:follows:";

    /**
     * 用户关注数据过期时间
     */
    Long USER_FOLLOWS_TTL = 30L;


    /**
     * 用户粉丝数据前缀
     */
    String USER_FANS = "user:fans:";

    /**
     * 用户粉丝数据过期时间
     */
    Long USER_FANS_TTL = 30L;



}
