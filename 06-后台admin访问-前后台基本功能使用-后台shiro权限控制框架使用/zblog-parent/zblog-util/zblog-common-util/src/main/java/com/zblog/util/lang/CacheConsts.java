package com.zblog.util.lang;

/**
 * 缓存常量类
 */
public interface CacheConsts {
    /**
     * 用户分库hash key
     */
    String utoken = "utoken";

    /**
     * 用户登录状态数据 key
     */
    String token = "token";

    /**
     * shiro用户信息
     */
    String shiroSession = "shiro_redis_session";
}
