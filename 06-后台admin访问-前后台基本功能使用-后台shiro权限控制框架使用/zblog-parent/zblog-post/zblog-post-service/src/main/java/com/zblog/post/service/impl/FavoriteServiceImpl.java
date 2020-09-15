package com.zblog.post.service.impl;


import com.github.pagehelper.PageInfo;
import com.zblog.post.service.FavoriteService;
import com.zblog.post.utils.PostBeanMapUtils;
import com.zblog.post.vo.FavoriteVO;
import com.zblog.post.vo.PostVO;
import com.zblog.sharedb.dao.subtreasury.MtoFavoriteDao;
import com.zblog.sharedb.dao.subtreasury.MtoPostDao;
import com.zblog.sharedb.entity.MtoComment;
import com.zblog.sharedb.entity.MtoFavorite;
import com.zblog.sharedb.entity.MtoPost;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.util.tool.PageHelpers;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author langhsu on 2015/8/31.
 */
@Service
@Transactional(readOnly = true)
public class FavoriteServiceImpl implements FavoriteService {
    Logger log = LoggerFactory.getLogger(FavoriteServiceImpl.class);

    @Autowired
    private MtoFavoriteDao mtoFavoriteDao;
    @Autowired
    private MtoPostDao mtoPostDao;

    @Override
    public DataPageVO<FavoriteVO> pagingByUserId(PageModuls pageModuls, long userId) {
        PageHelpers.startPage(pageModuls);
        List<MtoFavorite> page = mtoFavoriteDao.findAllByUserId(userId);

        List<FavoriteVO> rets = new ArrayList<>();
        Set<Long> postIds = new HashSet<>();
        for (MtoFavorite po : page) {
            rets.add(PostBeanMapUtils.copy(po));
            postIds.add(po.getPostId());
        }

        if (postIds.size() > 0) {
            List<Long> postIdsl = new ArrayList<>();
            postIds.forEach(id -> postIdsl.add(id));
            List<MtoPost> mtoPosts = mtoPostDao.findAllById(postIdsl);
            Map<Long, PostVO> posts = new HashMap<>();
            mtoPosts.forEach(po -> posts.put(po.getId(),PostBeanMapUtils.copy(po)));

            for (FavoriteVO t : rets) {
                PostVO p = posts.get(t.getPostId());
                t.setPost(p);
            }
        }
        PageInfo<MtoFavorite> p = new PageInfo<>(page);

        return new DataPageVO<>(pageModuls, rets, p.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void add(long userId, long postId) {
        MtoFavorite po = mtoFavoriteDao.findByUserIdAndPostId(userId, postId);

        Assert.isNull(po, "您已经收藏过此文章");

        // 如果没有喜欢过, 则添加记录
        po = new MtoFavorite();
        po.setUserId(userId);
        po.setPostId(postId);
        po.setCreated(new Date());

        mtoFavoriteDao.insert(po);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(long userId, long postId) {
        MtoFavorite po = mtoFavoriteDao.findByUserIdAndPostId(userId, postId);
        Assert.notNull(po, "还没有喜欢过此文章");
        mtoFavoriteDao.deleteByPrimaryKey(po.getId());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteByPostId(long postId) {
        int rows = mtoFavoriteDao.deleteByPostId(postId);
        log.info("favoriteRepository delete {}", rows);
    }

}
