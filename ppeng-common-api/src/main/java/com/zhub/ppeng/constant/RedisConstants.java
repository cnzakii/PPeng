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
     * 验证码注册前缀
     */
    String REGISTER_CODE_KEY = "register:code:";

    /**
     * 验证码更新过期时间
     */
    Long REGISTER_CODE_TTL = 2L;

    /**
     * 验证码登录前缀
     */
    String LOGIN_CODE_KEY = "login:code:";


    /**
     * 验证码更新前缀
     */
    String UPDATE_CODE_KEY = "update:code:";

    /**
     * 验证码更新过期时间
     */
    Long UPDATE_CODE_TTL = 2L;


    /**
     * 用户角色前缀
     */
    String USER_ROLE = "user:role:";



    /**
     * 用户信息
     */
    String USER_INFO = "user:info:";

    /**
     * 用户基本信息过期时间
     */
    Long USER_INFO_TTL = 12L;


    /**
     * 用户角色信息过期时间
     */

    Long ROLE_USER_TTL = 12L;



    /**
     * 用户关注数据前缀
     */
    String USER_FOLLOWS_KEY = "user:follows:";

    /**
     * 用户关注数据过期时间
     */
    Long USER_FOLLOWS_TTL = 30L;


    /**
     * 用户粉丝数据前缀
     */
    String USER_FANS_KEY = "user:fans:";

    /**
     * 用户粉丝数据过期时间
     */
    Long USER_FANS_TTL = 30L;

    /**
     * 用户点赞菜谱数据前缀
     */
    String USER_LIKE_KEY = "user:like:";

    /**
     * 用户点赞菜谱数据过期时间
     */
    Long USER_LIKE_TTL = 30L;

    /**
     * 用户收藏菜谱数据前缀
     */
    String USER_COLLECT_KEY = "user:collect:";

    /**
     * 用户收藏菜谱数据过期时间
     */
    Long USER_COLLECT_TTL = 30L;



}
