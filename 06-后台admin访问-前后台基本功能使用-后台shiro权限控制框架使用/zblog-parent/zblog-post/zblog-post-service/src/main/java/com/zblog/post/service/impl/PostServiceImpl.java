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


import com.github.pagehelper.PageInfo;
import com.zblog.post.service.PostService;
import com.zblog.post.service.TagService;
import com.zblog.post.service.aspect.PostStatusFilter;
import com.zblog.post.service.event.PostUpdateEvent;
import com.zblog.post.utils.PostBeanMapUtils;
import com.zblog.post.vo.PostVO;
import com.zblog.sharedb.dao.master.MtoChannelDao;
import com.zblog.sharedb.dao.master.MtoTagDao;
import com.zblog.sharedb.dao.subtreasury.*;
import com.zblog.sharedb.entity.*;
import com.zblog.sharedb.vo.DataPageVO;
import com.zblog.sharedb.vo.UserVO;
import com.zblog.util.lang.Consts;
import com.zblog.sharedb.vo.PageModuls;
import com.zblog.util.tool.MarkdownUtils;
import com.zblog.util.tool.PageHelpers;
import com.zblog.util.tool.PreviewTextUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author langhsu
 *
 */
@Service
@Transactional
public class PostServiceImpl implements PostService {
	@Autowired
	private MtoPostDao mtoPostDao;
	@Autowired
	private MtoPostAttributeDao mtoPostAttributeDao;
	@Autowired
	private MtoUserDao mtoUserDao;
	@Autowired
	private MtoFavoriteDao mtoFavoriteDao;
	@Autowired
	private MtoChannelDao mtoChannelDao;
	@Autowired
	private TagService tagService;
	@Autowired
	private ApplicationContext applicationContext;
	@Autowired
	private MtoPostResourceDao mtoPostResourceDao;
	@Autowired
	private MtoResourceDao mtoResourceDao;
	@Autowired
	private RedissonClient redissonClient;

    private static Pattern pattern = Pattern.compile("(?<=/_signature/)(.+?)(?=\\.)");


	@Override
	@PostStatusFilter
	public DataPageVO<PostVO> paging0(PageModuls pageModuls, int channelId, Set<Integer> excludeChannelIds) {
		PageHelpers.startPage(pageModuls);
		List<Integer> excludeChannelIdsl = new ArrayList<>();
		excludeChannelIds.forEach(id -> excludeChannelIdsl.add(id));
		List<Integer> excludeChannelIdsll = excludeChannelIdsl;
		if(excludeChannelIds == null || excludeChannelIds.size() == 0){
			excludeChannelIdsll = null;
		}
		List<MtoPost> page = mtoPostDao.findAllByChannel(channelId,excludeChannelIdsll);
		PageInfo<MtoPost> p = new PageInfo<>(page);

		return new DataPageVO<>(pageModuls, toPosts(page), p.getTotal());
	}

	@Override
	@PostStatusFilter
	public DataPageVO<PostVO> paging(PageModuls pageModuls, int channelId, Set<Integer> excludeChannelIds) {
		PageHelpers.startPage(pageModuls);

		List<Integer> excludeChannelIdsl = new ArrayList<>();
		excludeChannelIds.forEach(id -> excludeChannelIdsl.add(id));
		List<MtoPost> page = mtoPostDao.findAllByChannel(channelId,excludeChannelIdsl);

		PageInfo<MtoPost> p = new PageInfo<>(page);
		return new DataPageVO<>(pageModuls, toPosts(page), p.getTotal());
	}

	@Override
	public DataPageVO<PostVO> paging4Admin(PageModuls pageModuls, int channelId, String title) {
		PageHelpers.startPage(pageModuls);
		List<MtoPost> page = mtoPostDao.findAllByChannelAndTile(channelId,title==null?null:"%"+title+"%");
		PageInfo<MtoPost> p = new PageInfo<>(page);
		return new DataPageVO<>(pageModuls, toPosts(page), p.getTotal());
	}

	@Override
	@PostStatusFilter
	public DataPageVO<PostVO> pagingByAuthorId(PageModuls pageModuls, long userId) {
		PageHelpers.startPage(pageModuls);
		List<MtoPost> page = mtoPostDao.findAllByAuthorId(userId);
		PageInfo<MtoPost> p = new PageInfo<>(page);
		return new DataPageVO<>(pageModuls, toPosts(page), p.getTotal());
	}

	@Override
	@PostStatusFilter
	public List<PostVO> findLatestPosts(int maxResults) {
		return find("created", maxResults).stream().map(PostBeanMapUtils::copy).collect(Collectors.toList());
	}
	
	@Override
	@PostStatusFilter
	public List<PostVO> findHottestPosts(int maxResults) {
		return find("views", maxResults).stream().map(PostBeanMapUtils::copy).collect(Collectors.toList());
	}
	
	@Override
	@PostStatusFilter
	public Map<Long, PostVO> findMapByIds(Set<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyMap();
		}


		List<Long> idsl = new ArrayList<>();
		ids.forEach(id -> idsl.add(id));

		List<MtoPost> list = mtoPostDao.findAllById0(idsl);
		Map<Long, PostVO> rets = new HashMap<>();

		HashSet<Long> uids = new HashSet<>();

		list.forEach(po -> {
			rets.put(po.getId(), PostBeanMapUtils.copy(po));
			uids.add(po.getAuthorId());
		});
		
		// 加载用户信息
		buildUsers(rets.values(), uids);
		return rets;
	}

	@Override
    @Transactional(rollbackFor = Throwable.class)
	public long post(PostVO post) {
		MtoPost po = new MtoPost();

		BeanUtils.copyProperties(post, po);

		po.setCreated(new Date());
		po.setStatus(post.getStatus());

		// 处理摘要
		if (StringUtils.isBlank(post.getSummary())) {
			po.setSummary(trimSummary(post.getEditor(), post.getContent()));
		} else {
			po.setSummary(post.getSummary());
		}

		int insert = mtoPostDao.insert(po);
		tagService.batchUpdate(po.getTags(), po.getId());

		//TODO 先改为分布式锁 后需要测试是否合理
		RLock lock = redissonClient.getLock(po.getId()+"");
        try {
			if(lock.tryLock()) {
				MtoPostAttribute attr = new MtoPostAttribute();
				attr.setContent(post.getContent());
				attr.setEditor(post.getEditor());
				attr.setId(po.getId());
				mtoPostAttributeDao.insert(attr);

				countResource(po.getId(), null, attr.getContent());
				onPushEvent(po, PostUpdateEvent.ACTION_PUBLISH);
				return po.getId();
			}
			return po.getId();
        }finally {
			lock.unlock();
        }
	}
	
	@Override
	public PostVO get(long id) {
		MtoPost po = mtoPostDao.selectByPrimaryKey(id);
		if (po != null) {
			PostVO d = PostBeanMapUtils.copy(po);

			d.setAuthor(PostBeanMapUtils.copy(mtoUserDao.selectByPrimaryKey(d.getAuthorId())));
			d.setChannel(mtoChannelDao.selectByPrimaryKey(d.getChannelId()));

			MtoPostAttribute attr = mtoPostAttributeDao.selectByPrimaryKey(d.getId());
			d.setContent(attr.getContent());
			d.setEditor(attr.getEditor());
			return d;
		}
		return null;
	}

	/**
	 * 更新文章方法
	 * @param p
	 */
	@Override
    @Transactional(rollbackFor = Throwable.class)
	public void update(PostVO p){
		MtoPost po = mtoPostDao.selectByPrimaryKey(p.getId());

		if (po != null) {
			RLock lock = redissonClient.getLock(po.getId()+"");
            try {
                if(lock.tryLock()){
                    po.setTitle(p.getTitle());//标题
                    po.setChannelId(p.getChannelId());
                    po.setThumbnail(p.getThumbnail());
                    po.setStatus(p.getStatus());

                    // 处理摘要
                    if (StringUtils.isBlank(p.getSummary())) {
                        po.setSummary(trimSummary(p.getEditor(), p.getContent()));
                    } else {
                        po.setSummary(p.getSummary());
                    }

                    po.setTags(p.getTags());//标签

                    // 保存扩展
                    MtoPostAttribute attributeOptional = mtoPostAttributeDao.selectByPrimaryKey(po.getId());
                    String originContent = "";

					MtoPostAttribute attr = new MtoPostAttribute();
					attr.setContent(p.getContent());
					attr.setEditor(p.getEditor());
					attr.setId(po.getId());
                    if (attributeOptional != null){
                        originContent = attributeOptional.getContent();
						mtoPostAttributeDao.updateByPrimaryKey(attr);
                    }else{
						mtoPostAttributeDao.insert(attr);
					}

                    tagService.batchUpdate(po.getTags(), po.getId());

                    countResource(po.getId(), originContent, p.getContent());
                }
            }finally {
				lock.unlock();
            }
		}
	}

	@Override
    @Transactional(rollbackFor = Throwable.class)
	public void updateFeatured(long id, int featured) {
		MtoPost po = mtoPostDao.selectByPrimaryKey(id);
		int status = Consts.FEATURED_ACTIVE == featured ? Consts.FEATURED_ACTIVE: Consts.FEATURED_DEFAULT;
		po.setFeatured(status);
		mtoPostDao.updateByPrimaryKey(po);
	}

	@Override
    @Transactional(rollbackFor = Throwable.class)
	public void updateWeight(long id, int weighted) {
		MtoPost po = mtoPostDao.selectByPrimaryKey(id);

		int max = Consts.ZERO;
		if (Consts.FEATURED_ACTIVE == weighted) {
			max = mtoPostDao.maxWeight() + 1;
		}
		po.setWeight(max);
		mtoPostDao.updateByPrimaryKey(po);
	}

	@Override
    @Transactional(rollbackFor = Throwable.class)
	public void delete(long id, long authorId) {
		MtoPost po = mtoPostDao.selectByPrimaryKey(id);
		// 判断文章是否属于当前登录用户
		Assert.isTrue(po.getAuthorId() == authorId, "认证失败");

		RLock lock = redissonClient.getLock(po.getId()+"");
		try	{
			if(lock.tryLock()){
				mtoPostDao.deleteByPrimaryKey(id);
				mtoPostAttributeDao.deleteByPrimaryKey(id);
				cleanResource(po.getId());
				onPushEvent(po, PostUpdateEvent.ACTION_DELETE);
			}
		}finally {
			lock.unlock();
		}
	}

	@Override
    @Transactional(rollbackFor = Throwable.class)
	public void delete(Collection<Long> ids) {
		if (CollectionUtils.isNotEmpty(ids)) {
			List<Long> idsl = new ArrayList<>();
			ids.forEach(id -> idsl.add(id));
			List<MtoPost> list = mtoPostDao.findAllById(idsl);
			list.forEach(po -> {
				RLock lock = redissonClient.getLock(po.getId()+"");
				try	{
					if(lock.tryLock()){
						mtoPostDao.deleteByPrimaryKey(po.getId());
						mtoPostAttributeDao.deleteByPrimaryKey(po.getId());
						cleanResource(po.getId());
						onPushEvent(po, PostUpdateEvent.ACTION_DELETE);
					}
				}finally {
					lock.unlock();
				}
			});
		}
	}

	@Override
    @Transactional(rollbackFor = Throwable.class)
	public void identityViews(long id) {
		// 不清理缓存, 等待文章缓存自动过期
		//todo 这种操作  要加redission锁
		MtoPost post = mtoPostDao.selectByPrimaryKey(id);
		post.setViews(post.getViews()+Consts.IDENTITY_STEP);
		mtoPostDao.updateByPrimaryKey(post);
	}

	@Override
	@Transactional
	public void identityComments(long id) {
		//todo 这种操作  要加redission锁
		MtoPost post = mtoPostDao.selectByPrimaryKey(id);
		post.setComments(post.getComments()+Consts.IDENTITY_STEP);
		mtoPostDao.updateByPrimaryKey(post);
	}

	@Override
    @Transactional(rollbackFor = Throwable.class)
	public void favor(long userId, long postId) {
		//todo 这种操作  要加redission锁
		MtoPost post = mtoPostDao.selectByPrimaryKey(postId);
		post.setFavors(post.getFavors()+Consts.IDENTITY_STEP);
		mtoPostDao.updateByPrimaryKey(post);

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
	public void unfavor(long userId, long postId) {
		//todo 这种操作  要加redission锁
		MtoPost post = mtoPostDao.selectByPrimaryKey(postId);
		post.setFavors(post.getFavors()+Consts.DECREASE_STEP);
		mtoPostDao.updateByPrimaryKey(post);

		MtoFavorite po = mtoFavoriteDao.findByUserIdAndPostId(userId, postId);
		mtoFavoriteDao.deleteByPrimaryKey(po.getId());
	}

	@Override
	@PostStatusFilter
	public long count() {
		return mtoPostDao.count();
	}

	@PostStatusFilter
	private List<MtoPost> find(String orderBy, int size) {

		Set<Integer> excludeChannelIds = new HashSet<>();

		List<MtoChannel> channels = mtoChannelDao.findAll(Consts.STATUS_CLOSED);
		if (channels != null) {
			channels.forEach((c) -> excludeChannelIds.add(c.getId()));
		}

		PageModuls pageModuls = new PageModuls();
		PageHelpers.startPage(pageModuls);

		List<Integer> excludeChannelIdsl = new ArrayList<>();
		excludeChannelIds.forEach(id -> excludeChannelIdsl.add(id));
		List<MtoPost> page = mtoPostDao.findAllByExcludeChannels(excludeChannelIdsl);
		return page;
	}

	/**
	 * 截取文章内容
	 * @param text
	 * @return
	 */
	private String trimSummary(String editor, final String text){
		if (Consts.EDITOR_MARKDOWN.endsWith(editor)) {
			return PreviewTextUtils.getText(MarkdownUtils.renderMarkdown(text), 126);
		} else {
			return PreviewTextUtils.getText(text, 126);
		}
	}

	private List<PostVO> toPosts(List<MtoPost> posts) {
		HashSet<Long> uids = new HashSet<>();
		HashSet<Integer> groupIds = new HashSet<>();

		List<PostVO> rets = posts
				.stream()
				.map(po -> {
					uids.add(po.getAuthorId());
					groupIds.add(po.getChannelId());
					return PostBeanMapUtils.copy(po);
				})
				.collect(Collectors.toList());

		// 加载用户信息
		buildUsers(rets, uids);
		buildGroups(rets, groupIds);

		return rets;
	}

	private void buildUsers(Collection<PostVO> posts, Set<Long> uids) {
		if(uids != null && uids.size()>0) {
			List<Long> uidsl = new ArrayList<>();
			uids.forEach(id -> uidsl.add(id));
			List<MtoUser> mtoUsers = mtoUserDao.findAllById(uidsl);
			Map<Long, UserVO> userMap = new HashMap<>();
			mtoUsers.forEach(po -> userMap.put(po.getId(), PostBeanMapUtils.copy(po)));
			posts.forEach(p -> p.setAuthor(userMap.get(p.getAuthorId())));
		}
	}

	private void buildGroups(Collection<PostVO> posts, Set<Integer> groupIds) {
		if(groupIds != null && groupIds.size()>0) {
			List<Integer> groupIdsl = new ArrayList<>();
			groupIds.forEach(id -> groupIdsl.add(id));
			List<MtoChannel> mtoChannels = mtoChannelDao.findAllById(groupIdsl);
			Map<Integer, MtoChannel> map = new HashMap<>();
			mtoChannels.forEach(po -> map.put(po.getId(), po));
			posts.forEach(p -> p.setChannel(map.get(p.getChannelId())));
		}
	}

	private void onPushEvent(MtoPost post, int action) {
		PostUpdateEvent event = new PostUpdateEvent(System.currentTimeMillis());
		event.setPostId(post.getId());
		event.setUserId(post.getAuthorId());
		event.setAction(action);
		//TODO 分布式情况下 这里要改用 MQ
		applicationContext.publishEvent(event);
	}

	private void countResource(Long postId, String originContent, String newContent){
	    if (StringUtils.isEmpty(originContent)){
	        originContent = "";
        }
        if (StringUtils.isEmpty(newContent)){
	        newContent = "";
        }

		Set<String> exists = extractImageMd5(originContent);
		Set<String> news = extractImageMd5(newContent);

        List<String> adds = ListUtils.removeAll(news, exists);
		List<String> deleteds = ListUtils.removeAll(exists, news);

		if (adds.size() > 0) {
			List<MtoResource> resources = mtoResourceDao.findByMd5In(adds);

			List<MtoPostResource> prs = resources.stream().map(n -> {
				MtoPostResource pr = new MtoPostResource();
				pr.setResourceId(n.getId());
				pr.setPostId(postId);
				pr.setPath(n.getPath());
				return pr;
			}).collect(Collectors.toList());
			mtoPostResourceDao.insertAll(prs);

			mtoResourceDao.updateAmount(adds, 1);
		}

		if (deleteds.size() > 0) {
			List<MtoResource> resources = mtoResourceDao.findByMd5In(deleteds);
			List<Long> rids = resources.stream().map(MtoResource::getId).collect(Collectors.toList());
			mtoPostResourceDao.deleteByPostIdAndResourceIdIn(postId, rids);
			mtoResourceDao.updateAmount(deleteds, -1);
		}
	}

	private void cleanResource(long postId) {
		List<MtoPostResource> list = mtoPostResourceDao.findByPostId(postId);
		if (null == list || list.isEmpty()) {
			return;
		}
		List<Long> rids = list.stream().map(MtoPostResource::getResourceId).collect(Collectors.toList());
		mtoResourceDao.updateAmountByIds(rids, -1);
		mtoPostResourceDao.deleteByPostId(postId);
	}

	private Set<String> extractImageMd5(String text) {
//		Pattern pattern = Pattern.compile("(?<=/_signature/)[^/]+?jpg");

		Set<String> md5s = new HashSet<>();

		Matcher originMatcher = pattern.matcher(text);
		while (originMatcher.find()) {
			String key = originMatcher.group();
//			md5s.add(key.substring(0, key.lastIndexOf(".")));
			md5s.add(key);
		}

		return md5s;
	}
}
