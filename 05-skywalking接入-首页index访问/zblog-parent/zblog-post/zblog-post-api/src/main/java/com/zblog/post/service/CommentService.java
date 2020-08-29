/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.zblog.post.service;

import com.zblog.post.vo.CommentVO;
import com.zblog.sharedb.entity.MtoComment;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author langhsu
 *
 */
public interface CommentService {
	DataPageVO<CommentVO> paging4Admin(PageModuls pageable);

	DataPageVO<CommentVO> pagingByAuthorId(PageModuls pageable, long authorId);

	/**
	 * 查询评论列表
	 * @param pageable
	 * @param postId
	 */
	DataPageVO<CommentVO> pagingByPostId(PageModuls pageable, long postId);

	List<CommentVO> findLatestComments(int maxResults);

	Map<Long, CommentVO> findByIds(Set<Long> ids);

	MtoComment findById(long id);
	
	/**
	 * 发表评论
	 * @param comment
	 * @return
	 */
	long post(CommentVO comment);
	
	void delete(List<Long> ids);

	void delete(long id, long authorId);

	void deleteByPostId(long postId);

	long count();

	long countByAuthorIdAndPostId(long authorId, long postId);
}
