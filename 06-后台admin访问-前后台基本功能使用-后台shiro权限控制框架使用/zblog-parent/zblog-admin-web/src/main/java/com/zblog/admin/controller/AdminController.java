/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.zblog.admin.controller;


import com.zblog.basics.service.UserService;
import com.zblog.post.service.ChannelService;
import com.zblog.post.service.CommentService;
import com.zblog.post.service.PostService;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.util.conf.Result;
import com.zblog.util.lang.CacheConsts;
import com.zblog.util.sharedb.DynamicDataSourceContextHolder;
import com.zblog.util.tool.CookieUtil;
import com.zblog.util.tool.JSONUtil;
import com.zblog.util.tool.MD5;
import com.zblog.util.tool.TokenUtil;
import com.zblog.web.annotations.LoginRequired;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author langhsu
 *
 */
@Controller
public class AdminController {
    @Reference
    private ChannelService channelService;
    @Reference
    private PostService postService;
    @Reference
    private CommentService commentService;
    @Reference
    private UserService userService;

	@RequestMapping("/admin")
    @LoginRequired
	public String index(HttpServletRequest request, ModelMap model) {

		pushSystemStatus(request, model);
		model.put("channelCount", channelService.count());
        model.put("postCount", postService.count());
        model.put("commentCount", commentService.count());
        model.put("userCount", userService.count());
		return "/admin/index";
	}



    private void pushSystemStatus(HttpServletRequest request, ModelMap model) {
        float freeMemory = (float) Runtime.getRuntime().freeMemory();
        float totalMemory = (float) Runtime.getRuntime().totalMemory();
        float usedMemory = (totalMemory - freeMemory);
        float memPercent = Math.round(freeMemory / totalMemory * 100) ;
        String os = System.getProperty("os.name");
        String javaVersion = System.getProperty("java.version");

        model.addAttribute("freeMemory", freeMemory);
        model.addAttribute("totalMemory", totalMemory / 1024 / 1024);
        model.addAttribute("usedMemory", usedMemory / 1024 / 1024);
        model.addAttribute("memPercent", memPercent);
        model.addAttribute("os", os);
        model.addAttribute("javaVersion", javaVersion);
	}


}
