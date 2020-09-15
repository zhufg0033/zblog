/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.zblog.index.controller.auth;


import com.zblog.index.controller.Views;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.util.lang.CacheConsts;
import com.zblog.util.sharedb.DynamicDataSourceContextHolder;
import com.zblog.util.tool.CookieUtil;
import com.zblog.util.tool.JSONUtil;
import com.zblog.util.tool.TokenUtil;
import com.zblog.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author langhsu
 */
@Controller
public class LogoutController extends BaseController {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    /**
     * 退出登录
     *
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request,  HttpServletResponse response) {
        //删除客户端，redis缓存
        String token = null;
        Cookie cookie = CookieUtil.getCookie(request, TokenUtil.token);
        if(cookie != null && StringUtils.isNotBlank(cookie.getValue())){
            token = cookie.getValue();
        }
        if(token != null) {
            String apstr = (String) redisTemplate.opsForHash().get(CacheConsts.token, token);
            if(apstr != null) {
                redisTemplate.opsForHash().delete(CacheConsts.token, token);
            }
            CookieUtil.deleteCookie(request,response,cookie);

        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        response.setDateHeader("Expires", 0); // Proxies.
        return Views.REDIRECT_INDEX;
    }

}
