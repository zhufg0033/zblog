package com.zblog.basics.service.commpont;

import com.zblog.basics.dao.MtoUserDao;
import com.zblog.basics.db.DataSourceKey;
import com.zblog.basics.db.DynamicDataSourceContextHolder;
import com.zblog.basics.po.MtoUser;
import com.zblog.basics.service.impl.UserServiceImpl;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Test1Commpont {

    Logger log = LoggerFactory.getLogger(Test1Commpont.class);

    @Autowired
    private MtoUserDao mtoUserDao;

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW) // 开启新事物
    public void updateUserInfoTest1() throws Exception{
        log.info("=============updateUserInfoTest1=================");
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.zblog2019);
        log.info("当前 XID: {}", RootContext.getXID());
        MtoUser mtoUser = mtoUserDao.selectByPrimaryKey(2l);
        mtoUser.setEmail("888");
        mtoUserDao.updateByPrimaryKey(mtoUser);
        System.out.println("6666");
    }
}
