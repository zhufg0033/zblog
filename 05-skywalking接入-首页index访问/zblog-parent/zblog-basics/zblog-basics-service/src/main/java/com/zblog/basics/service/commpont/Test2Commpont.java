package com.zblog.basics.service.commpont;

import com.zblog.sharedb.dao.MtoUserDao;
import com.zblog.sharedb.entity.MtoUser;
import com.zblog.util.sharedb.DataSourceKey;
import com.zblog.util.sharedb.DynamicDataSourceContextHolder;
import io.seata.core.context.RootContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class Test2Commpont {

    Logger log = LoggerFactory.getLogger(Test1Commpont.class);

    @Autowired
    private MtoUserDao mtoUserDao;

    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW) // 开启新事物
    public void updateUserInfoTest() throws Exception{
        log.info("=============updateUserInfoTest=================");
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.zblog2020);
        log.info("当前 XID: {}", RootContext.getXID());
        MtoUser mtoUser = mtoUserDao.selectByPrimaryKey(10000004l);
        mtoUser.setEmail("222");
        mtoUserDao.updateByPrimaryKey(mtoUser);
        System.out.println(mtoUser.toString());

        int x = 0;
//        if(x<1){
//            throw new Exception("可以回滚了老弟");
//        }
    }
}
