package com.zblog;


import com.zblog.basics.service.UserService;
import com.zblog.basics.service.impl.UserServiceImpl;
import com.zblog.sharedb.dao.MtoUserDao;
import io.seata.spring.annotation.GlobalTransactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZblogBasicsServiceApplicationTests {

    @Autowired
    MtoUserDao mtoUserDao;

    @Autowired
    UserServiceImpl userService;

    @Test
    public void contextLoads() {

    }

    @Test
    public void contextLoads2020() {

    }

}