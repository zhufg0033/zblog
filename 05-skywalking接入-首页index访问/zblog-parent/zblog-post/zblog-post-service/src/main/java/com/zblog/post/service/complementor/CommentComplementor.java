package com.zblog.post.service.complementor;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.zblog.post.service.CommentService;
import com.zblog.post.service.PostService;
import com.zblog.post.utils.PostBeanMapUtils;
import com.zblog.post.vo.CommentVO;
import com.zblog.post.vo.PostVO;
import com.zblog.sharedb.dao.MtoUserDao;
import com.zblog.sharedb.entity.MtoComment;
import com.zblog.sharedb.entity.MtoUser;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.tool.SpringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : zhufg
 * @version : 1.0
 * @date : 2020/08/26
 */
public class CommentComplementor {
    private List<CommentVO> comments = Lists.newArrayList();
    private Set<Long> userIds = Sets.newHashSet();
    private Set<Long> postIds = Sets.newHashSet();
    private Set<Long> parentIds = Sets.newHashSet();

    public static CommentComplementor of(List<MtoComment> entities) {
        CommentComplementor builder = new CommentComplementor();

        entities.forEach(po -> {
            if (po.getPid() > 0) {
                builder.parentIds.add(po.getPid());
            }
            builder.userIds.add(po.getAuthorId());
            builder.postIds.add(po.getPostId());
            builder.comments.add(PostBeanMapUtils.copy(po));
        });

        return builder;
    }

    public CommentComplementor flutBuildUser() {
        List<Long> userIdsl = new ArrayList<>();
        this.userIds.forEach(id -> userIdsl.add(id));
        List<MtoUser> allById = SpringUtils.getBean(MtoUserDao.class).findAllById(userIdsl);
        Map<Long, UserVO> map = new HashMap<>();
        allById.forEach(po -> map.put(po.getId(),PostBeanMapUtils.copy(po)));
        comments.forEach(p -> p.setAuthor(map.get(p.getAuthorId())));
        return this;
    }

    public CommentComplementor flutBuildPost() {
        Map<Long, PostVO> map = SpringUtils.getBean(PostService.class).findMapByIds(this.postIds);
        comments.forEach(p -> p.setPost(map.get(p.getPostId())));
        return this;
    }

    public CommentComplementor flutBuildParent() {
        if (!parentIds.isEmpty()) {
            Map<Long, CommentVO> pm = SpringUtils.getBean(CommentService.class).findByIds(parentIds);

            comments.forEach(c -> {
                if (c.getPid() > 0) {
                    c.setParent(pm.get(c.getPid()));
                }
            });
        }
        return this;
    }

    public List<CommentVO> getComments() {
        return comments;
    }

    public Map<Long, CommentVO> toMap() {
        return comments.stream().collect(Collectors.toMap(CommentVO::getId, n-> n));
    }

}
