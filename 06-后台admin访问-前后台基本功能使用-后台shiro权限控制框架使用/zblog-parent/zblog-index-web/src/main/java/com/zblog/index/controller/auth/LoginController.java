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


import com.zblog.basics.service.UserRoleService;
import com.zblog.basics.service.UserService;
import com.zblog.index.controller.Views;
import com.zblog.sharedb.entity.ShiroPermission;
import com.zblog.sharedb.entity.ShiroRole;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.sharedb.vo.PermissionInfo;
import com.zblog.util.conf.Result;
import com.zblog.util.lang.CacheConsts;
import com.zblog.util.tool.CookieUtil;
import com.zblog.util.tool.JSONUtil;
import com.zblog.util.tool.MD5;
import com.zblog.util.tool.TokenUtil;
import com.zblog.web.controller.BaseController;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 登录
 * @author zhufugao
 *
 */
@Controller
public class LoginController extends BaseController {

    @Reference
    UserService userService;

    @Reference
    UserRoleService userRoleService;

    /**
     * 跳转登录页
     * @return
     */
	@GetMapping(value = "/login")
	public String view() {
		return view(Views.LOGIN);
	}



    /**
     * 提交登录
     * @param username
     * @param password
     * @param model
     * @return
     */
	@PostMapping(value = "/login")
	public String login(HttpServletRequest request, HttpServletResponse response,
                        String username,
                        String password,
                        @RequestParam(value = "rememberMe",defaultValue = "0") Boolean rememberMe,
                        ModelMap model) {
		String view = view(Views.LOGIN);

        Result<AccountProfile> result = userService.login(username, MD5.md5(password));
        if (result.isOk()) {

            setRoleInfo(result);

            AfterLoginHandler(request,response,result,model);

            view = String.format(Views.REDIRECT_USER_HOME, result.getData().getId());
        } else {
            model.put("message", result.getMessage());
        }
        return view;


	}



}
