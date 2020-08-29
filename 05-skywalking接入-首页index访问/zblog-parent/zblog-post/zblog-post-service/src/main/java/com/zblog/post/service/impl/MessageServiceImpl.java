package com.zblog.post.service.impl;


import com.zblog.post.service.MessageService;
import com.zblog.post.utils.PostBeanMapUtils;
import com.zblog.post.vo.MessageVO;
import com.zblog.post.vo.PostVO;
import com.zblog.sharedb.dao.MtoMessageDao;
import com.zblog.sharedb.dao.MtoPostDao;
import com.zblog.sharedb.dao.MtoUserDao;
import com.zblog.sharedb.entity.MtoMessage;
import com.zblog.sharedb.entity.MtoPost;
import com.zblog.sharedb.entity.MtoUser;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.lang.Consts;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author langhsu
 */
@Service
@Transactional(readOnly = true)
public class MessageServiceImpl implements MessageService {
    @Autowired
    private MtoMessageDao mtoMessageDao;
    @Autowired
    private MtoUserDao mtoUserDao;
    @Autowired
    private MtoPostDao mtoPostDao;

    @Override
    public DataPageVO<MessageVO> pagingByUserId(PageModuls pageModuls, long userId) {
        List<MtoMessage> page = mtoMessageDao.findAllByUserId(userId);

        Set<Long> postIds = new HashSet<>();
        Set<Long> fromUserIds = new HashSet<>();

        // 筛选
        List<MessageVO> rets = page
                .stream()
                .map(po -> {
                    if (po.getPostId() > 0) {
                        postIds.add(po.getPostId());
                    }
                    if (po.getFromId() > 0) {
                        fromUserIds.add(po.getFromId());
                    }

                    return PostBeanMapUtils.copy(po);
                })
                .collect(Collectors.toList());

        // 加载
        List<Long> postIdsl = new ArrayList<>();
        postIds.forEach(id -> postIdsl.add(id));
        List<MtoPost> mtoPosts = mtoPostDao.findAllById(postIdsl);
        Map<Long, PostVO> posts = new HashMap<>();
        mtoPosts.forEach(po -> posts.put(po.getId(),PostBeanMapUtils.copy(po)));

        List<Long> fromUserIdsl = new ArrayList<>();
        fromUserIds.forEach(id -> fromUserIdsl.add(id));
        List<MtoUser> mtoUsers = mtoUserDao.findAllById(fromUserIdsl);
        Map<Long, UserVO> fromUsers = new HashMap<>();
        mtoUsers.forEach(po -> fromUsers.put(po.getId(),PostBeanMapUtils.copy(po)));

        rets.forEach(n -> {
            if (n.getPostId() > 0) {
                n.setPost(posts.get(n.getPostId()));
            }
            if (n.getFromId() > 0) {
                n.setFrom(fromUsers.get(n.getFromId()));
            }
        });

        return new DataPageVO<>(pageModuls, rets, rets.size());
    }

    @Override
    @Transactional
    public void send(MessageVO message) {
        if (message == null || message.getUserId() <=0 || message.getFromId() <= 0) {
            return;
        }

        MtoMessage po = new MtoMessage();
        BeanUtils.copyProperties(message, po);
        po.setCreated(new Date());

        mtoMessageDao.insert(po);
    }

    @Override
    public int unread4Me(long userId) {
        return mtoMessageDao.countByUserIdAndStatus(userId, Consts.UNREAD);
    }

    @Override
    @Transactional
    public void readed4Me(long userId) {
        mtoMessageDao.updateReadedByUserId(userId);
    }

    @Override
    @Transactional
    public int deleteByPostId(long postId) {
        return mtoMessageDao.deleteByPostId(postId);
    }
}
