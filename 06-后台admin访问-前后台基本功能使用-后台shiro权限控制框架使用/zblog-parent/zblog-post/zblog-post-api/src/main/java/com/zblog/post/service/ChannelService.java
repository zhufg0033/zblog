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


import com.zblog.sharedb.entity.MtoChannel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 栏目管理
 * 
 * @author langhsu
 *
 */
public interface ChannelService {
	List<MtoChannel> findAll(int status);
	Map<Integer, MtoChannel> findMapByIds(Collection<Integer> ids);
	MtoChannel getById(int id);
	void update(MtoChannel channel);
	void updateWeight(int id, int weighted);
	void delete(int id);
	long count();
}
