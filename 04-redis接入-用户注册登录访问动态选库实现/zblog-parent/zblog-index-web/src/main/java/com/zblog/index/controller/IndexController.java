package com.zblog.index.controller;

import com.zblog.basics.service.UserService;
import com.zblog.basics.vo.AccountProfile;
import com.zblog.basics.vo.UserVO;
import com.zblog.util.lang.CacheConsts;
import com.zblog.util.lang.Consts;
import com.zblog.util.tool.CookieUtil;
import com.zblog.util.tool.JSONUtil;
import com.zblog.util.tool.MD5;
import com.zblog.util.tool.TokenUtil;
import com.zblog.web.annotations.LoginRequired;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class IndexController {

    Logger logger = LoggerFactory.getLogger(IndexController.class);

    @Reference
    UserService userService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Value("${loginMaxAge:3600}")
    int loginMaxAge;

    @LoginRequired
    @RequestMapping("getUsername")
    @ResponseBody
    public String getUsername(HttpServletRequest request) throws Exception {


        String token = CookieUtil.getCookie(request, TokenUtil.token).getValue();
        String apstr = (String) redisTemplate.opsForHash().get(CacheConsts.token, token);
        System.out.println(apstr);
        AccountProfile accountProfile = JSONUtil.parseObject(apstr,AccountProfile.class);
        String signDb = accountProfile.getSignDb();

        String userName = userService.getUserName(accountProfile.getUsername(),signDb);
        System.out.println("userService.getUserName():"+userName);

        return userName;
    }

    @RequestMapping("login")
    @ResponseBody
    public String login(String username,
                        String password,
                        @RequestParam(value = "rememberMe",defaultValue = "0") Boolean rememberMe,
                        ModelMap model,
                        HttpServletRequest request, HttpServletResponse response) {
        AccountProfile accountProfile = userService.login(username, MD5.md5(password));

        AfterLoginHandler(request,response,accountProfile);
        return null;
    }

    @RequestMapping(value = "register",method = RequestMethod.POST)
    @ResponseBody
    public String register(UserVO post, HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        post.setAvatar(Consts.AVATAR);
        userService.register(post);
        AccountProfile accountProfile = userService.login(post.getUsername(), MD5.md5(post.getPassword()));
        AfterLoginHandler(request,response,accountProfile);

        return "success";
    }

    private String AfterLoginHandler(HttpServletRequest request, HttpServletResponse response,AccountProfile accountProfile){
        //缓存中记录用户信息  设置cookie
        String token = TokenUtil.createToken();
        redisTemplate.opsForHash().put(CacheConsts.token,token, JSONUtil.toJSONString(accountProfile));

        CookieUtil.setCookie(request,response,TokenUtil.token,token,loginMaxAge);
        return null;
    }

}
