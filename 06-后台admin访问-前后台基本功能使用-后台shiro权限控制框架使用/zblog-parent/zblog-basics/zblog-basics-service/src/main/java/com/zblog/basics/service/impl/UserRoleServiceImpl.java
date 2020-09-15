package com.zblog.basics.service.impl;


import com.zblog.basics.service.UserRoleService;
import com.zblog.sharedb.dao.master.ShiroPermissionDao;
import com.zblog.sharedb.dao.master.ShiroRoleDao;
import com.zblog.sharedb.dao.master.ShiroRolePermissionDao;
import com.zblog.sharedb.dao.subtreasury.ShiroUserRoleDao;
import com.zblog.sharedb.entity.ShiroPermission;
import com.zblog.sharedb.entity.ShiroRole;
import com.zblog.sharedb.entity.ShiroRolePermission;
import com.zblog.sharedb.entity.ShiroUserRole;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author - langhsu on 2018/2/11
 */
@Service
@Transactional(readOnly = true)
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private ShiroUserRoleDao shiroUserRoleDao;
    @Autowired
    private ShiroRoleDao shiroRoleDao;

    @Autowired
    private ShiroRolePermissionDao shiroRolePermissionDao;

    @Autowired
    private ShiroPermissionDao shiroPermissionDao;



    @Override
    public List<Long> listRoleIds(long userId) {
        List<ShiroUserRole> list = shiroUserRoleDao.findAllByUserId(userId);
        List<Long> roleIds = new ArrayList<>();
        if (null != list) {
            list.forEach(po -> roleIds.add(po.getRoleId()));
        }
        return roleIds;
    }

    @Override
    public List<ShiroRole> listRoles(long userId) {
        List<Long> roleIds = listRoleIds(userId);
        List<ShiroRole> allByIds = shiroRoleDao.findAllByIds(roleIds);
        Map<Long,ShiroRole> map = new LinkedHashMap<>();
        allByIds.forEach(po -> {
            ShiroRole vo = toVO(po);
            map.put(po.getId(),vo);
        });

        return new ArrayList(map.values());
    }

    @Override
    public Map<Long, List<ShiroRole>> findMapByUserIds(List<Long> userIds) {
        List<ShiroUserRole> list = shiroUserRoleDao.findAllByUserIdIn(userIds);
        Map<Long, List<Long>> map = new HashMap<>();

        list.forEach(po -> {
            List<Long> roleIds = map.computeIfAbsent(po.getUserId(), k -> new ArrayList<>());
            roleIds.add(po.getRoleId());
        });

        Map<Long, List<ShiroRole>> ret = new HashMap<>();
        map.forEach((k, v) -> {
            ret.put(k, new ArrayList(shiroRoleDao.findAllByIds(v)));
        });
        return ret;
    }

    @Override
    @Transactional
    public void updateRole(long userId, Set<Long> roleIds) {
        // 判断是否清空已授权角色
        if (null == roleIds || roleIds.isEmpty()) {
            shiroUserRoleDao.deleteByUserId(userId);
        } else {
            List<ShiroUserRole> list = shiroUserRoleDao.findAllByUserId(userId);
            List<Long> exitIds = new ArrayList<>();

            // 如果已有角色不在 新角色列表中, 执行删除操作
            if (null != list) {
                list.forEach(po -> {
                    if (!roleIds.contains(po.getRoleId())) {
                        shiroUserRoleDao.deleteByPrimaryKey(po.getId());
                    } else {
                        exitIds.add(po.getRoleId());
                    }
                });
            }

            // 保存不在已有角色中的新角色ID
            roleIds.stream().filter(id -> !exitIds.contains(id)).forEach(roleId -> {
                ShiroUserRole po = new ShiroUserRole();
                po.setUserId(userId);
                po.setRoleId(roleId);

                shiroUserRoleDao.insert(po);
            });
        }


    }

    private ShiroRole toVO(ShiroRole po) {
        ShiroRole r = new ShiroRole();
        r.setId(po.getId());
        r.setName(po.getName());
        r.setDescription(po.getDescription());
        r.setStatus(po.getStatus());

        List<ShiroRolePermission> allByRoleIds = shiroRolePermissionDao.findAllByRoleId(r.getId());
        List<ShiroPermission> rets = null;
        if(allByRoleIds!=null && allByRoleIds.size()>0){
            List<Long> pids = new ArrayList<>();
            allByRoleIds.forEach(po2 -> pids.add(po2.getPermissionId()));

            List<ShiroPermission> allByIds = shiroPermissionDao.findAllByIds(pids);

            r.setPermissions(allByIds);
        }

        return r;
    }
}
