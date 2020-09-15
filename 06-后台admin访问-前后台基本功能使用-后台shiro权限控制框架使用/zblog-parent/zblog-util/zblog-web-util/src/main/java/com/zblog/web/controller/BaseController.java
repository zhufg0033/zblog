/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.zblog.web.controller;

import com.zblog.basics.service.UserRoleService;
import com.zblog.basics.service.UserService;
import com.zblog.post.service.StorageFactory;
import com.zblog.sharedb.entity.ShiroPermission;
import com.zblog.sharedb.entity.ShiroRole;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.sharedb.vo.PermissionInfo;
import com.zblog.util.conf.Result;
import com.zblog.util.conf.SiteOptions;
import com.zblog.util.lang.CacheConsts;
import com.zblog.util.tool.CookieUtil;
import com.zblog.util.tool.JSONUtil;
import com.zblog.util.tool.MD5;
import com.zblog.util.tool.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Controller 基类
 *
 * @author langhsu
 * @since 3.0
 */
@Slf4j
public class BaseController {
    @Reference
    protected StorageFactory storageFactory;
    @Autowired
    protected SiteOptions siteOptions;

    @Reference
    UserService userService;

    @Reference
    UserRoleService userRoleService;

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Value("${loginMaxAge:3600}")
    int loginMaxAge;

    @InitBinder
    public void initBinder(ServletRequestDataBinder binder) {
        /**
         * 自动转换日期类型的字段格式
         */
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), true));

//        /**
//         * 防止XSS攻击
//         */
//        binder.registerCustomEditor(String.class, new StringEscapeEditor(true, false));
    }

    /**
     * 获取登录信息
     *
     * @return
     */
    protected AccountProfile getProfile(HttpServletRequest request) {
        String token = CookieUtil.getCookie(request, TokenUtil.token).getValue();
        String apstr = (String) redisTemplate.opsForHash().get(CacheConsts.token, token);
        System.out.println(apstr);
        AccountProfile accountProfile = JSONUtil.parseObject(apstr,AccountProfile.class);
        return accountProfile;
    }

//
//    protected boolean isAuthenticated() {
//        return SecurityUtils.getSubject() != null && (SecurityUtils.getSubject().isAuthenticated() || SecurityUtils.getSubject().isRemembered());
//    }

    protected PageModuls wrapPageable() {
        return wrapPageable(null);
    }

    protected PageModuls wrapPageable(String sortStr) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        int pageSize = ServletRequestUtils.getIntParameter(request, "pageSize", 10);
        int pageNo = ServletRequestUtils.getIntParameter(request, "pageNo", 1);

        return new PageModuls(pageNo, pageSize, sortStr);
    }

    /**
     * 包装分页对象
     *
     * @param pn 页码
     * @param pn 页码
     * @return
     */
    protected PageModuls wrapPageable(Integer pn, Integer pageSize) {
        if (pn == null || pn == 0) {
            pn = 1;
        }
        if (pageSize == null || pageSize == 0) {
            pageSize = 10;
        }
        return new PageModuls(pn, pageSize ,null);
    }

    protected String view(String view) {
        return "/" + siteOptions.getValue("theme") + view;
    }


    protected  void setRoleInfo(Result<AccountProfile> result ){
        List<ShiroRole> shiroRoles = userRoleService.listRoles(result.getData().getId());
        final String[] checkAdmin = {"none"};
        if(shiroRoles!=null){
            shiroRoles.forEach(po ->{
                List<ShiroPermission> list = po.getPermissions();
                if(list !=null) {
                    list.forEach(po2 -> {
                        if (ShiroRole.ROLE_ADMIN.equals(po2.getName())){
                            checkAdmin[0] = "admin";
                        }
                    });
                }
            });
        }
        PermissionInfo permissionInfo = new PermissionInfo();
        permissionInfo.setIsAdmin(checkAdmin[0]);
        result.getData().setPermissionInfo(permissionInfo);

    }

    /**
     * 登录后处理  记录cookie
     * @param request
     * @param response
     * @param result
     */
    protected void AfterLoginHandler(HttpServletRequest request, HttpServletResponse response, Result<AccountProfile> result , ModelMap model){
        if(result.isOk()) {

            //缓存中记录用户信息  设置cookie
            String token = TokenUtil.createToken();
            redisTemplate.opsForHash().put(CacheConsts.token, token, JSONUtil.toJSONString(result.getData()));

            CookieUtil.setCookie(request, response, TokenUtil.token, token, loginMaxAge);

            model.put("profile",result.getData());
        }
    }

    protected Result<AccountProfile> executeLogin(String username, String password, boolean rememberMe) {

        Result<AccountProfile> ret = Result.failure("登录失败");

        if (StringUtils.isAnyBlank(username, password)) {
            return ret;
        }

        ret = userService.login(username, password);

        return ret;
    }
}
