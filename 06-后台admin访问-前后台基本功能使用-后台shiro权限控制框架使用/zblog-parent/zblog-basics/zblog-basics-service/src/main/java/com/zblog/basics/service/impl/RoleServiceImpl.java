package com.zblog.basics.service.impl;


import com.github.pagehelper.PageInfo;
import com.zblog.basics.service.RoleService;
import com.zblog.sharedb.dao.master.ShiroPermissionDao;
import com.zblog.sharedb.dao.master.ShiroRoleDao;
import com.zblog.sharedb.dao.master.ShiroRolePermissionDao;
import com.zblog.sharedb.dao.subtreasury.ShiroUserRoleDao;
import com.zblog.sharedb.entity.ShiroPermission;
import com.zblog.sharedb.entity.ShiroRole;
import com.zblog.sharedb.entity.ShiroRolePermission;
import com.zblog.sharedb.entity.ShiroUserRole;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.util.tool.PageHelpers;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author - langhsu on 2018/2/11
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private ShiroRoleDao shiroRoleDao;
    @Autowired
    private ShiroPermissionDao shiroPermissionDao;
    @Autowired
    private ShiroRolePermissionDao shiroRolePermissionDao;
    @Autowired
    private ShiroUserRoleDao shiroUserRoleDao;

    @Override
    public DataPageVO<ShiroRole> paging(PageModuls pageModuls, String name) {
        PageHelpers.startPage(pageModuls);
        List<ShiroRole> page = shiroRoleDao.findAll(name==null?null:"%"+name+"%");
        PageInfo<ShiroRole> p = new PageInfo<>(page);
        return new DataPageVO<>(pageModuls, page , p.getTotal());
    }

    @Override
    public List<ShiroRole> list() {
        List<ShiroRole> list = shiroRoleDao.findAllByStatus(ShiroRole.STATUS_NORMAL);
        return list;
    }

    @Override
    public Map<Long, ShiroRole> findByIds(List<Long> ids) {
        List<ShiroRole> list = shiroRoleDao.findAllByIds(ids);
        Map<Long, ShiroRole> ret = new LinkedHashMap<>();
        list.forEach(po -> {
            ShiroRole vo = toVO(po);
            ret.put(vo.getId(), vo);
        });
        return ret;
    }

    @Override
    public ShiroRole get(long id) {
        return toVO(shiroRoleDao.selectByPrimaryKey(id));
    }

    @Override
    public void update(ShiroRole r, Set<ShiroPermission> permissions) {
        ShiroRole optional = shiroRoleDao.selectByPrimaryKey(r.getId());
        ShiroRole po = optional==null?new ShiroRole():optional;
        po.setName(r.getName());
        po.setDescription(r.getDescription());
        po.setStatus(r.getStatus());
        if(optional == null)
            shiroRoleDao.insert(po);
        else
            shiroRoleDao.updateByPrimaryKey(po);

        shiroRolePermissionDao.deleteByRoleId(po.getId());

        if (permissions != null && permissions.size() > 0) {
            List<ShiroRolePermission> rps = new ArrayList<>();
            long roleId = po.getId();
            permissions.forEach(p -> {
                ShiroRolePermission rp = new ShiroRolePermission();
                rp.setRoleId(roleId);
                rp.setPermissionId(p.getId());
                rps.add(rp);
            });

            shiroRolePermissionDao.insertAll(rps);
        }
    }

    @Override
    public boolean delete(long id) {
        List<ShiroUserRole> urs = shiroUserRoleDao.findAllByRoleId(id);
        Assert.state(urs == null || urs.size() == 0, "该角色已经被使用,不能被删除");
        shiroRoleDao.deleteByPrimaryKey(id);
        shiroRolePermissionDao.deleteByRoleId(id);
        return true;
    }

    @Override
    public void activate(long id, boolean active) {
        ShiroRole po = shiroRoleDao.selectByPrimaryKey(id);
        po.setStatus(active ? ShiroRole.STATUS_NORMAL : ShiroRole.STATUS_CLOSED);
    }

    private ShiroRole toVO(ShiroRole po) {
        ShiroRole r = new ShiroRole();
        r.setId(po.getId());
        r.setName(po.getName());
        r.setDescription(po.getDescription());
        r.setStatus(po.getStatus());

        List<ShiroRolePermission> rps = shiroRolePermissionDao.findAllByRoleId(r.getId());

        List<ShiroPermission> rets = null;
        if (rps != null && rps.size() > 0) {
            List<Long> pids = new ArrayList<>();
            rps.forEach(rp -> pids.add(rp.getPermissionId()));
            rets = shiroPermissionDao.findAllByIds(pids);
        }

        r.setPermissions(rets);
        return r;
    }
}
