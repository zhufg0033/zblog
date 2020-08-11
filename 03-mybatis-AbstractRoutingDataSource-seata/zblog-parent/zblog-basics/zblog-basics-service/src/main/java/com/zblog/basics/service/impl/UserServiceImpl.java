package com.zblog.basics.service.impl;

import com.zblog.basics.dao.MtoUserDao;
import com.zblog.basics.db.DataSourceKey;
import com.zblog.basics.db.DynamicDataSourceContextHolder;
import com.zblog.basics.db.DynamicRoutingDataSource;
import com.zblog.basics.po.MtoUser;
import com.zblog.basics.service.UserService;
import com.zblog.basics.service.commpont.Test1Commpont;
import com.zblog.basics.service.commpont.Test2Commpont;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${userNameTest}")
    private String userName;

    @Autowired
    private Test1Commpont test1Commpont;

    @Autowired
    private Test2Commpont test2Commpont;

    @Override
    @GlobalTransactional
    public String getUserName() throws Exception{
        log.info("=============getUserName=================");
        DynamicDataSourceContextHolder.setDataSourceKey(DataSourceKey.zblog2019);
        log.info("当前 XID: {}", RootContext.getXID());
        test1Commpont.updateUserInfoTest1();

        test2Commpont.updateUserInfoTest();



        return userName;
    }






//    public void contextLoads2020() {
//        MtoUser mtoUser = new MtoUser();
//        mtoUser.setName("测试用户");
//        mtoUser.setStatus(0);
//        mtoUser.setGender(1);
//        mtoUser.setComments(0);
//        mtoUser.setPosts(0);
//        mtoUserDao.insert(mtoUser);
//    }



}
