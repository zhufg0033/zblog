package com.zblog.post.service;

import com.zblog.post.vo.MessageVO;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;

/**
 * @author langhsu
 */
public interface MessageService {

    DataPageVO<MessageVO> pagingByUserId(PageModuls pageModuls, long userId);

    /**
     * 发送通知
     * @param message
     */
    void send(MessageVO message);

    /**
     * 未读消息数量
     * @param userId
     * @return
     */
    int unread4Me(long userId);

    /**
     * 标记为已读
     * @param userId
     */
    void readed4Me(long userId);

    /**
     * 根据文章ID清理消息
     * @param postId
     * @return
     */
    int deleteByPostId(long postId);
}
