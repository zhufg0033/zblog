package com.zblog.basics.service;

import com.zblog.sharedb.entity.ShiroRole;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author - langhsu on 2018/2/11
 */
public interface UserRoleService {
    /**
     * 查询用户已有的角色Id
     * @param userId 用户ID
     * @return
     */
    List<Long> listRoleIds(long userId);

    /**
     * 查询用户已有的角色 和 权限
     * @param userId 用户ID
     * @return
     */
    List<ShiroRole> listRoles(long userId);

    Map<Long, List<ShiroRole>> findMapByUserIds(List<Long> userIds);

    /**
     * 修改用户角色
     * @param userId 用户ID
     * @param roleIds 要授权的角色ID
     */
    void updateRole(long userId, Set<Long> roleIds);
}
