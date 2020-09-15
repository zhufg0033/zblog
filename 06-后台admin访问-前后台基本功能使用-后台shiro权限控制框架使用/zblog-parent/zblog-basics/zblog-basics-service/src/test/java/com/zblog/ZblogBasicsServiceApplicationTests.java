package com.zblog;


import com.zblog.basics.service.impl.UserServiceImpl;
import com.zblog.sharedb.dao.subtreasury.MtoUserDao;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan(value = "com.zblog.sharedb.dao.subtreasury",sqlSessionFactoryRef = "sqlSessionFactoryBean")
@MapperScan(value = "com.zblog.sharedb.dao.master",sqlSessionFactoryRef = "sqlSessionFactoryDefault")
public class ZblogBasicsServiceApplicationTests {


    @Reference
    UserServiceImpl userService;

    @Test
    public void contextLoads() {
//        mtoUserDao
    }

    @Test
    public void contextLoads2020() {

    }

}