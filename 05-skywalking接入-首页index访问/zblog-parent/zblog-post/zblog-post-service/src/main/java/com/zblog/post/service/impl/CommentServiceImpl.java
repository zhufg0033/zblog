/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.zblog.post.service.impl;

import com.zblog.post.service.CommentService;
import com.zblog.post.service.complementor.CommentComplementor;
import com.zblog.post.vo.CommentVO;
import com.zblog.sharedb.dao.MtoCommentDao;
import com.zblog.sharedb.dao.MtoUserDao;
import com.zblog.sharedb.entity.MtoComment;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.util.tool.PageHelpers;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * @author langhsu
 *
 */
@Service
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {
	@Autowired
	private MtoCommentDao mtoCommentDao;
	//TODO 在网关整合 userService和userEventService
//	@Autowired
//	private UserService userService;
//	@Autowired
//	private UserEventService userEventService;
//	@Autowired
//	private PostService postService;
	@Autowired
	private MtoUserDao mtoUserDao;

	@Override
	public DataPageVO<CommentVO> paging4Admin(PageModuls pageModuls) {
		PageHelpers.startPage(pageModuls);
		List<MtoComment> page = mtoCommentDao.selectAll();
		if(page==null){
			return null;
		}
		List<CommentVO> rets = CommentComplementor.of(page)
				.flutBuildUser()
				.getComments();
		return new DataPageVO<>(pageModuls,rets,rets.size());
	}

	@Override
	public DataPageVO<CommentVO> pagingByAuthorId(PageModuls pageModuls, long authorId) {
		List<MtoComment> page = mtoCommentDao.findAllByAuthorId(authorId);

		if(page==null){
			return null;
		}

		List<CommentVO> rets = CommentComplementor.of(
				page)
				.flutBuildUser()
				.flutBuildParent()
				.flutBuildPost()
				.getComments();
		return new DataPageVO<>(pageModuls,rets,rets.size());
	}

	@Override
	public DataPageVO<CommentVO> pagingByPostId(PageModuls pageModuls, long postId) {
		PageHelpers.startPage(pageModuls);
		List<MtoComment> page = mtoCommentDao.findAllByPostId(postId);

		if(page == null){
			return null;
		}

		List<CommentVO> rets = CommentComplementor.of(page)
				.flutBuildUser()
				.flutBuildParent()
				.getComments();
		return new DataPageVO<>(pageModuls,rets,rets.size());
	}

	@Override
	public List<CommentVO> findLatestComments(int maxResults) {
		List<MtoComment> page = mtoCommentDao.selectAll();
		return CommentComplementor.of(page)
				.flutBuildUser()
				.getComments();
	}

	@Override
	public Map<Long, CommentVO> findByIds(Set<Long> ids) {
		List<MtoComment> list = mtoCommentDao.findAllById(ids);
		return CommentComplementor.of(list)
				.flutBuildUser()
				.toMap();
	}

	@Override
	public MtoComment findById(long id) {
		return mtoCommentDao.selectByPrimaryKey(id);
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public long post(CommentVO comment) {
		MtoComment po = new MtoComment();
		
		po.setAuthorId(comment.getAuthorId());
		po.setPostId(comment.getPostId());
		po.setContent(comment.getContent());
		po.setCreated(new Date());
		po.setPid(comment.getPid());
		mtoCommentDao.insert(po);

		//TODO 网关event操作
//		userEventService.identityComment(comment.getAuthorId(), true);
		return po.getId();
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void delete(List<Long> ids) {
		mtoCommentDao.removeByIdIn(ids);
//		if (CollectionUtils.isNotEmpty(list)) {
//			list.forEach(po -> {
//				userEventService.identityComment(po.getAuthorId(), false);
//			});
//		}
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void delete(long id, long authorId) {
		MtoComment po = mtoCommentDao.selectByPrimaryKey(id);
		if (po != null ) {
			// 判断文章是否属于当前登录用户
			Assert.isTrue(po.getAuthorId() == authorId, "认证失败");
			mtoCommentDao.deleteByPrimaryKey(id);

//			userEventService.identityComment(authorId, false);
		}
	}

	@Override
	@Transactional(rollbackFor = Throwable.class)
	public void deleteByPostId(long postId) {
		mtoCommentDao.removeByPostId(postId);
//		if (CollectionUtils.isNotEmpty(list)) {
//			Set<Long> userIds = new HashSet<>();
//			list.forEach(n -> userIds.add(n.getAuthorId()));
////			userEventService.identityComment(userIds, false);
//		}
	}

	@Override
	public long count() {
		return mtoCommentDao.count();
	}

	@Override
	public long countByAuthorIdAndPostId(long authorId, long toId) {
		return mtoCommentDao.countByAuthorIdAndPostId(authorId, toId);
	}

}
