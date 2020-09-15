package com.zblog.post.service;


import com.zblog.post.vo.PostTagVO;
import com.zblog.post.vo.TagVO;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author : langhsu
 */
public interface TagService {
    DataPageVO<TagVO> pagingQueryTags(PageModuls pageModuls);
    DataPageVO<PostTagVO> pagingQueryPosts(PageModuls pageModuls, String tagName);
    void batchUpdate(String names, long latestPostId);
    void deteleMappingByPostId(long postId);
}
