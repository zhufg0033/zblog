package com.zblog.post.service.impl;


import com.github.pagehelper.PageInfo;
import com.zblog.post.service.PostService;
import com.zblog.post.service.TagService;
import com.zblog.post.utils.PostBeanMapUtils;
import com.zblog.post.vo.PostTagVO;
import com.zblog.post.vo.PostVO;
import com.zblog.post.vo.TagVO;
import com.zblog.sharedb.dao.master.MtoTagDao;
import com.zblog.sharedb.dao.subtreasury.MtoPostTagDao;
import com.zblog.sharedb.entity.MtoMessage;
import com.zblog.sharedb.entity.MtoPostTag;
import com.zblog.sharedb.entity.MtoTag;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.util.lang.Consts;
import com.zblog.util.tool.PageHelpers;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : langhsu
 */
@Service
@Transactional(readOnly = true)
public class TagServiceImpl implements TagService {
    @Autowired
    private MtoTagDao mtoTagDao;
    @Autowired
    private MtoPostTagDao mtoPostTagDao;
    @Autowired
    private PostService postService;

    @Override
    public DataPageVO<TagVO> pagingQueryTags(PageModuls pageModuls) {
        PageHelpers.startPage(pageModuls);
        List<MtoTag> list = mtoTagDao.selectAll();

        Set<Long> postIds = new HashSet<>();
        List<TagVO> rets = list.stream().map(po -> {
            postIds.add(po.getLatestPostId());
            return PostBeanMapUtils.copy(po);
        }).collect(Collectors.toList());

        Map<Long, PostVO> posts = postService.findMapByIds(postIds);
        rets.forEach(n -> n.setPost(posts.get(n.getLatestPostId())));

        PageInfo<MtoTag> p = new PageInfo<>(list);
        return new DataPageVO<>(pageModuls, rets, p.getTotal());
    }

    @Override
    public DataPageVO<PostTagVO> pagingQueryPosts(PageModuls pageModuls, String tagName) {
        MtoTag tag = mtoTagDao.findByName(tagName);
        Assert.notNull(tag, "标签不存在");

        PageHelpers.startPage(pageModuls);
        List<MtoPostTag> list = mtoPostTagDao.findAllByTagId(tag.getId());

        Set<Long> postIds = new HashSet<>();
        List<PostTagVO> rets = list.stream().map(po -> {
            postIds.add(po.getPostId());
            return PostBeanMapUtils.copy(po);
        }).collect(Collectors.toList());

        Map<Long, PostVO> posts = postService.findMapByIds(postIds);
        rets.forEach(n -> n.setPost(posts.get(n.getPostId())));

        PageInfo<MtoPostTag> p = new PageInfo<>(list);
        return new DataPageVO<>(pageModuls, rets, p.getTotal());
    }

    @Override
    @Transactional
    public void batchUpdate(String names, long latestPostId) {
        if (StringUtils.isBlank(names.trim())) {
            return;
        }

        String[] ns = names.split(Consts.SEPARATOR);
        Date current = new Date();
        for (String n : ns) {
            String name = n.trim();
            if (StringUtils.isBlank(name)) {
                continue;
            }

            MtoTag po = mtoTagDao.findByName(name);
            if (po != null) {
                MtoPostTag pt = mtoPostTagDao.findByPostIdAndTagId(latestPostId, po.getId());
                if (null != pt) {
                    pt.setWeight(System.currentTimeMillis());
                    mtoPostTagDao.updateByPrimaryKey(pt);
                    continue;
                }
                po.setPosts(po.getPosts() + 1);
                po.setUpdated(current);
                po.setLatestPostId(latestPostId);
                mtoTagDao.updateByPrimaryKey(po);
            } else {
                po = new MtoTag();
                po.setName(name);
                po.setCreated(current);
                po.setUpdated(current);
                po.setPosts(1);
                po.setLatestPostId(latestPostId);
                mtoTagDao.insert(po);
            }

            MtoPostTag pt = new MtoPostTag();
            pt.setPostId(latestPostId);
            pt.setTagId(po.getId());
            pt.setWeight(System.currentTimeMillis());
            mtoPostTagDao.insert(pt);
        }
    }

    @Override
    @Transactional
    public void deteleMappingByPostId(long postId) {
        List<Long> tagIds = mtoPostTagDao.findTagIdByPostId(postId);
        if (CollectionUtils.isNotEmpty(tagIds)) {
            mtoTagDao.decrementPosts(tagIds);
        }
        mtoPostTagDao.deleteByPostId(postId);
    }
}
