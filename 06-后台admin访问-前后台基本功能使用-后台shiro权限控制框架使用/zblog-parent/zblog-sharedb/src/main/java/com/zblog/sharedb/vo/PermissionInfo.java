package com.zblog.sharedb.vo;

import java.io.Serializable;

/**
 * @ClassName : PermissionInfo  //类名
 * @Description : 权限信息  //描述
 * @Author : zhufugao  //作者
 * @Date: 2020-09-10 15:43  //时间
 */
public class PermissionInfo implements Serializable {
    private static final long serialVersionUID = 8276459939240769412L;

    private String isAdmin = "none"; //是否是管理员

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
}
