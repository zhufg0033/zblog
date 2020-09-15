package com.zblog.admin.shiro;


import com.zblog.basics.service.UserRoleService;
import com.zblog.basics.service.UserService;
import com.zblog.sharedb.entity.ShiroRole;
import com.zblog.sharedb.vo.AccountProfile;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.lang.CacheConsts;
import com.zblog.util.lang.Consts;
import com.zblog.util.tool.JSONUtil;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

public class AccountRealm extends AuthorizingRealm {
    @Reference
    private UserService userService;
    @Reference
    private UserRoleService userRoleService;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public AccountRealm() {
        super(new AllowAllCredentialsMatcher());
        setAuthenticationTokenClass(UsernamePasswordToken.class);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        AccountProfile profile = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        if (profile != null) {
            UserVO user = userService.get(profile.getId());
            if (user != null) {
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                List<ShiroRole> roles = userRoleService.listRoles(user.getId());

                //赋予角色
                roles.forEach(role -> {
                    info.addRole(role.getName());

                    //赋予权限
                    role.getPermissions().forEach(permission -> info.addStringPermission(permission.getName()));
                });
                return info;
            }
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;

        String apstr = new String(upToken.getPassword());
        AccountProfile profile = JSONUtil.parseObject(apstr, AccountProfile.class);
//        AccountProfile profile = getAccount(userService, token);

        if (null == profile) {
            throw new UnknownAccountException(upToken.getUsername());
        }

        if (profile.getStatus() == Consts.STATUS_CLOSED) {
            throw new LockedAccountException(profile.getName());
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(profile, token.getCredentials(), getName());
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("profile", profile);

        redisTemplate.opsForHash().put(CacheConsts.shiroSession,profile.getId()+"",session.getId());

        return info;
    }

//    protected AccountProfile getAccount(UserService userService, AuthenticationToken token) {
//        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
//        return userService.login(upToken.getUsername(), String.valueOf(upToken.getPassword())).getData();
//    }
}
