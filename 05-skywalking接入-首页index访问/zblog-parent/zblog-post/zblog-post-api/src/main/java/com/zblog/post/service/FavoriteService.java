package com.zblog.post.service;

import com.zblog.post.vo.FavoriteVO;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;

/**
 * 收藏记录
 * @author langhsu
 */
public interface FavoriteService {
    /**
     * 查询用户收藏记录
     * @param pageable
     * @param userId
     * @return
     */
    DataPageVO<FavoriteVO> pagingByUserId(PageModuls pageModuls, long userId);

    void add(long userId, long postId);
    void delete(long userId, long postId);
    void deleteByPostId(long postId);
}
