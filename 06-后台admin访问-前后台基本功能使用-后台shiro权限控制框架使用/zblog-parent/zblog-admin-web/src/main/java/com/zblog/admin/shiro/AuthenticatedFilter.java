/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.zblog.admin.shiro;

import java.io.IOException;
import java.util.Formatter;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.util.conf.Result;
import com.zblog.util.lang.CacheConsts;
import com.zblog.util.sharedb.DynamicDataSourceContextHolder;
import com.zblog.util.tool.CookieUtil;
import com.zblog.util.tool.JSONUtil;
import com.zblog.util.tool.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @version 1.0.0
 */
public class AuthenticatedFilter extends OncePerRequestFilter {
    private static final String JS = "<script type='text/javascript'>var wp=window.parent; if(wp!=null){while(wp.parent&&wp.parent!==wp){wp=wp.parent;}wp.location.href='%1$s';}else{window.location.href='%1$s';}</script>";
    private String loginUrl = "http://localhost:8071/login";

    private RedisTemplate<String,String> redisTemplate;;

    public AuthenticatedFilter(RedisTemplate<String,String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        //先判断登录缓存
        checkLogin((HttpServletRequest) request);


        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated() || subject.isRemembered() || doLogin((HttpServletRequest) request)) {
            chain.doFilter(request, response);
        } else {

            WebUtils.saveRequest(request);
            String path = WebUtils.getContextPath((HttpServletRequest) request);
            String url = loginUrl;
            if (StringUtils.isNotBlank(path) && path.length() > 1) {
                url = path + url;
            }

            if (isAjaxRequest((HttpServletRequest) request)) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().print(JSON.toJSONString(Result.failure("您还没有登录!")));
            } else {
		response.setContentType("text/html;charset=UTF-8");
                response.getWriter().write(new Formatter().format(JS, url).toString());
            }
        }
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
    
	/**
	 * 判断是否为Ajax请求 <功能详细描述>
	 * 
	 * @param request
	 * @return 是true, 否false
	 * @see [类、类#方法、类#成员]
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		String header = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(header);
	}

	public boolean checkLogin(HttpServletRequest request){
        Cookie cookie = CookieUtil.getCookie(request, TokenUtil.token);
        String token = null;
        if(cookie != null && StringUtils.isNotBlank(cookie.getValue())){
            token = cookie.getValue();
        }
        if(token != null) {

            String apstr = (String) redisTemplate.opsForHash().get(CacheConsts.token, token);
            if(apstr != null) {
                return true;
            }
        }
        SecurityUtils.getSubject().logout();
        return false;
    }

    public boolean doLogin(HttpServletRequest request){
        Cookie cookie = CookieUtil.getCookie(request, TokenUtil.token);
        String token = null;
        if(cookie != null && StringUtils.isNotBlank(cookie.getValue())){
            token = cookie.getValue();
        }
        if(token != null) {

            String apstr = (String) redisTemplate.opsForHash().get(CacheConsts.token, token);
            if(apstr != null) {
                AccountProfile accountProfile = JSONUtil.parseObject(apstr, AccountProfile.class);
                //进行切库变量存储到threadlocal   在dubbo的flter中 传到服务端进行切库
                DynamicDataSourceContextHolder.setDataSourceKey(accountProfile.getSignDb());


                //apstr
                UsernamePasswordToken uptoken = new UsernamePasswordToken(accountProfile.getUsername(), apstr, true);
                SecurityUtils.getSubject().login(uptoken);
                return SecurityUtils.getSubject().isAuthenticated();
            }
        }
        return false;
    }

}
