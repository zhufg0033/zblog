/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.zblog.basics.utils;


import com.zblog.basics.vo.AccountProfile;
import com.zblog.basics.vo.UserVO;
import com.zblog.sharedb.entity.MtoUser;
import org.springframework.beans.BeanUtils;

/**
 * @author langhsu
 */
public class BeanMapUtils {
    private static String[] USER_IGNORE = new String[]{"password", "extend", "roles"};

    public static UserVO copy(MtoUser po) {
        if (po == null) {
            return null;
        }
        UserVO ret = new UserVO();
        BeanUtils.copyProperties(po, ret, USER_IGNORE);
        return ret;
    }

    public static AccountProfile copyPassport(MtoUser po) {
        AccountProfile passport = new AccountProfile(po.getUsername(),
                po.getAvatar(),
                po.getName(),
                po.getEmail(),
                po.getLastLogin(),
                po.getStatus(),
                null,po.getSignDb());
        passport.setName(po.getName());
        passport.setEmail(po.getEmail());
        passport.setAvatar(po.getAvatar());
        passport.setLastLogin(po.getLastLogin());
        passport.setStatus(po.getStatus());
        passport.setSignDb(po.getSignDb());
        return passport;
    }

}
