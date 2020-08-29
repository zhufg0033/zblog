package com.zblog.basics.service.impl;

import com.zblog.basics.service.OptionsService;
import com.zblog.sharedb.dao.MtoOptionsDao;
import com.zblog.sharedb.entity.MtoOptions;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author langhsu
 */
@Service
public class OptionsServiceImpl implements OptionsService {
	@Autowired
	private MtoOptionsDao mtoOptionsDao;
//	@Autowired
//	private EntityManager entityManager;

	@Override
	@Transactional(readOnly = true)
	public List<MtoOptions> findAll() {
		List<MtoOptions> list = mtoOptionsDao.selectAll();
		List<MtoOptions> rets = new ArrayList<>();
		
		for (MtoOptions po : list) {
			MtoOptions r = new MtoOptions();
			BeanUtils.copyProperties(po, r);
			rets.add(r);
		}
		return rets;
	}

	@Override
	@Transactional
	public void update(Map<String, String> options) {
		if (options == null) {
			return;
		}

		//TODO 修改
//		options.forEach((key, value) -> {
//			MtoOptions entity = mtoOptionsDao.findByKey(key);
//			String val = StringUtils.trim(value);
//			if (entity != null) {
//				entity.setValue(val);
//			} else {
//				entity = new MtoOptions();
//				entity.setKey(key);
//				entity.setValue(val);
//			}
//			mtoOptionsDao.updateByPrimaryKey(entity);
//		});
	}

	@Override
	@Transactional
	public void initSettings(Resource resource) {
//		Session session = entityManager.unwrap(Session.class);
//		session.doWork(connection -> ScriptUtils.executeSqlScript(connection, resource));
	}

}
