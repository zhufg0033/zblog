package com.zblog.post.service;

import com.zblog.post.vo.PostVO;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author : langhsu
 * @version : 1.0
 * @date : 2019/1/18
 */
public interface PostSearchService {
    /**
     * 根据关键字搜索
     * @param pageModuls 分页
     * @param term 关键字
     * @throws Exception
     */
    DataPageVO<PostVO> search(PageModuls pageModuls, String term) throws Exception;

    /**
     * 重建
     */
    void resetIndexes();
}
