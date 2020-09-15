/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.zblog.index.controller.user;

import com.zblog.basics.service.UserService;
import com.zblog.index.controller.Views;
import com.zblog.post.service.MessageService;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.util.conf.Result;
import com.zblog.util.exception.MtonsException;
import com.zblog.web.annotations.LoginRequired;
import com.zblog.web.controller.BaseController;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户主页
 *
 * @author langhsu
 */
@Controller
@RequestMapping("/users")
public class UsersController extends BaseController {
    @Reference
    private UserService userService;
    @Reference
    private MessageService messageService;

    /**
     * 用户文章
     * @param userId 用户ID
     * @param model  ModelMap
     * @return template name
     */
    @GetMapping(value = "/{userId}")
    @LoginRequired
    public String posts(@PathVariable(value = "userId") Long userId,
                        ModelMap model, HttpServletResponse response, HttpServletRequest request) {
        return method(userId, Views.METHOD_POSTS, model, response, request);
    }

    /**
     * 通用方法, 访问 users 目录下的页面
     * @param userId 用户ID
     * @param method 调用方法
     * @param model  ModelMap
     * @return template name
     */
    @GetMapping(value = "/{userId}/{method}")
    @LoginRequired
    public String method(@PathVariable(value = "userId") Long userId,
                         @PathVariable(value = "method") String method,
                         ModelMap model, HttpServletResponse response, HttpServletRequest request) {
        model.put("pageNo", ServletRequestUtils.getIntParameter(request, "pageNo", 1));

        // 访问消息页, 判断登录
        if (Views.METHOD_MESSAGES.equals(method)) {
            // 标记已读
            AccountProfile profile = getProfile(request);
            if (null == profile || profile.getId() != userId) {
                throw new MtonsException("您没有权限访问该页面");
            }
            messageService.readed4Me(profile.getId());
        }

        initUser(response, request, userId, model);
        return view(String.format(Views.USER_METHOD_TEMPLATE, method));
    }

    private void initUser(HttpServletResponse response, HttpServletRequest request, long userId, ModelMap model) {
        model.put("user", userService.findProfile(userId).getData());
        boolean owner = false;

        AccountProfile profile = getProfile(request);
        if (null != profile && profile.getId() == userId) {
            owner = true;
            AfterLoginHandler(request, response, Result.success(profile), model);
        }
        model.put("owner", owner);
    }

}
