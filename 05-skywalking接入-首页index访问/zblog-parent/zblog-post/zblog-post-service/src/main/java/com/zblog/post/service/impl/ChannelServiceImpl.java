package com.zblog.post.service.impl;

import com.zblog.post.service.ChannelService;
import com.zblog.sharedb.dao.MtoChannelDao;
import com.zblog.sharedb.entity.MtoChannel;
import com.zblog.util.lang.Consts;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author langhsu
 *
 */
@Service
@Transactional(readOnly = true)
public class ChannelServiceImpl implements ChannelService {
	@Autowired
	private MtoChannelDao mtoChannelDao;

	@Override
	public List<MtoChannel> findAll(int status) {
		List<MtoChannel> list;
		if (status > Consts.IGNORE) {
			list = mtoChannelDao.findAllByStatus(status);
		} else {
			list = mtoChannelDao.selectAll();
		}
		return list;
	}

	@Override
	public Map<Integer, MtoChannel> findMapByIds(Collection<Integer> ids) {
		List<Integer> idsl = new ArrayList<>();
		ids.forEach(id -> idsl.add(id));
		List<MtoChannel> list = mtoChannelDao.findAllById(idsl);
		if (null == list) {
			return Collections.emptyMap();
		}
		return list.stream().collect(Collectors.toMap(MtoChannel::getId, n -> n));
	}

	@Override
	public MtoChannel getById(int id) {
		return mtoChannelDao.selectByPrimaryKey(id);
	}

	@Override
	@Transactional
	public void update(MtoChannel channel) {
		MtoChannel mtoChannel = mtoChannelDao.selectByPrimaryKey(channel.getId());
		if(mtoChannel == null){
			mtoChannelDao.insert(channel);
		}else{
			mtoChannelDao.updateByPrimaryKey(channel);
		}
	}

	@Override
	@Transactional
	public void updateWeight(int id, int weighted) {
		MtoChannel po = mtoChannelDao.selectByPrimaryKey(id);

		int max = Consts.ZERO;
		if (Consts.FEATURED_ACTIVE == weighted) {
			max = mtoChannelDao.maxWeight() + 1;
		}
		po.setWeight(max);
		mtoChannelDao.updateByPrimaryKey(po);
	}

	@Override
	@Transactional
	public void delete(int id) {
		mtoChannelDao.deleteByPrimaryKey(id);
	}

	@Override
	public long count() {
		return mtoChannelDao.count();
	}

}
