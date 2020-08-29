package com.zblog.post.service.impl;

import com.zblog.post.service.LinksService;
import com.zblog.sharedb.dao.MtoLinksDao;
import com.zblog.sharedb.entity.MtoLinks;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.util.tool.PageHelpers;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author : landy
 * @version : 1.0
 * @date : 2019/11/6
 */
@Service
@Transactional(readOnly = true)
public class LinksServiceImpl implements LinksService {
    @Autowired
    private MtoLinksDao mtoLinksDao;

    @Override
    public List<MtoLinks> findAll() {
        PageModuls pageModuls = new PageModuls(0,1000,"id "+Sort.by(Sort.Direction.DESC));
        PageHelpers.startPage(pageModuls);
        return mtoLinksDao.selectAll();
    }

    @Override
    @Transactional
    public void update(MtoLinks links) {
        MtoLinks po = mtoLinksDao.selectByPrimaryKey(links.getId());
        if(po == null) po = new MtoLinks();
        BeanUtils.copyProperties(links, po, "created", "updated");
        if(po == null)
            mtoLinksDao.insert(po);
        else
            mtoLinksDao.updateByPrimaryKey(po);
    }

    @Override
    @Transactional
    public void delete(long id) {
        mtoLinksDao.deleteByPrimaryKey(id);
    }
}
