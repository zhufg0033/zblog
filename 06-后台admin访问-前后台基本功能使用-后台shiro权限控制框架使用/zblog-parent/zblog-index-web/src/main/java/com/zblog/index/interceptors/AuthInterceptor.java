package com.zblog.index.interceptors;

import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.util.lang.CacheConsts;
import com.zblog.util.sharedb.DataSourceKey;
import com.zblog.util.sharedb.DynamicDataSourceContextHolder;
import com.zblog.util.tool.CookieUtil;
import com.zblog.util.tool.JSONUtil;
import com.zblog.util.tool.TokenUtil;
import com.zblog.web.annotations.LoginRequired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  拦截需要登录的情况，以及登录用户的切库逻辑处理
 */
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断被拦截的请求的访问的方法的注解（是否是需要被拦截的)
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        LoginRequired methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequired.class);


        //进行默认主库设置
        DynamicDataSourceContextHolder.setMasterDataSourceKey();



        String token = null;

        //验证cookie中的token有效
        boolean checktoken = false;
        Cookie cookie = CookieUtil.getCookie(request, TokenUtil.token);
        if(cookie != null && StringUtils.isNotBlank(cookie.getValue())){
            token = cookie.getValue();
        }
        if(token != null) {
            String apstr = (String) redisTemplate.opsForHash().get(CacheConsts.token, token);
            if(apstr != null) {
                AccountProfile accountProfile = JSONUtil.parseObject(apstr, AccountProfile.class);
                //赋值登录信息
                request.setAttribute("profile",accountProfile);

                //进行切库变量存储到threadlocal   在dubbo的flter中 传到服务端进行切库
                DynamicDataSourceContextHolder.setDataSourceKey(accountProfile.getSignDb());
                checktoken = true;
            }
        }

        if(methodAnnotation == null){
            return true;
        }

        //是否必须登录
        boolean loginSuccess = methodAnnotation.loginSuccess();
        //不需要必须登录
        if(!loginSuccess){
            return true;
        }
        //需要必须登录 token也是有效的情况下
        if(checktoken){
            return true;
        }
        return false;
    }
}
