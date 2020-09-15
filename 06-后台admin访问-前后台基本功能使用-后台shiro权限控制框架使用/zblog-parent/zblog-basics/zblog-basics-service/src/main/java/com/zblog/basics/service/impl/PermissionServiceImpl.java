package com.zblog.basics.service.impl;


import com.github.pagehelper.PageInfo;
import com.zblog.basics.service.PermissionService;
import com.zblog.basics.vo.PermissionTree;
import com.zblog.sharedb.dao.master.ShiroPermissionDao;
import com.zblog.sharedb.entity.MtoComment;
import com.zblog.sharedb.entity.ShiroPermission;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.util.tool.PageHelpers;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author - langhsu on 2018/2/11
 */
@Service
@Transactional(readOnly = true)
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private ShiroPermissionDao shiroPermissionDao;

    String sort = "weight desc,id asc";

    @Override
    public DataPageVO<ShiroPermission> paging(PageModuls pageModuls, String name) {
        pageModuls.setSort("id desc");
        PageHelpers.startPage(pageModuls);
        List<ShiroPermission> page = shiroPermissionDao.findAll(name);
        PageInfo<ShiroPermission> p = new PageInfo<>(page);
        return new DataPageVO<>(pageModuls, page, p.getTotal());
    }

    @Override
    public List<PermissionTree> tree() {
        PageModuls pageModuls = new PageModuls();
        pageModuls.setSort(sort);
        PageHelpers.startPage(pageModuls);
        List<ShiroPermission> data = shiroPermissionDao.findAll("");
        List<PermissionTree> results = new LinkedList<>();
        Map<Long, PermissionTree> map = new LinkedHashMap<>();

        for (ShiroPermission po : data) {
            PermissionTree m = new PermissionTree();
            BeanUtils.copyProperties(po, m);
            map.put(po.getId(), m);
        }

        for (PermissionTree m : map.values()) {
            if (m.getParentId() == 0) {
                results.add(m);
            } else {
                PermissionTree p = map.get(m.getParentId());
                if (p != null) {
                    p.addItem(m);
                }
            }
        }

        return results;
    }

    @Override
    public List<PermissionTree> tree(int parentId) {
        PageModuls pageModuls = new PageModuls();
        pageModuls.setSort(sort);
        PageHelpers.startPage(pageModuls);
        List<ShiroPermission> list = shiroPermissionDao.findAllByParentId(parentId);
        List<PermissionTree> results = new ArrayList<>();

        list.forEach(po -> {
            PermissionTree menu = new PermissionTree();
            BeanUtils.copyProperties(po, menu);
            results.add(menu);
        });
        return results;
    }

    @Override
    public List<ShiroPermission> list() {
        PageModuls pageModuls = new PageModuls();
        pageModuls.setSort(sort);
        PageHelpers.startPage(pageModuls);
        return shiroPermissionDao.selectAll();
    }


    @Override
    public ShiroPermission get(long id) {
        return shiroPermissionDao.selectByPrimaryKey(id);
    }

}
