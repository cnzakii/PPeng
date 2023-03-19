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
    String LOGIN_CODE_KEY = "login:code:";

    Long LOGIN_CODE_TTL = 2L;


    String ROLE_KEY = "role:";

    Long ROLE_TTL = 30L;


}
